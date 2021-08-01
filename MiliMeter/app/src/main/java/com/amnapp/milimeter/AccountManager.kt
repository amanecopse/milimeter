package com.amnapp.milimeter

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.amnapp.milimeter.activities.LoginActivity
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.nio.charset.Charset
import java.security.MessageDigest

class AccountManager {

    fun deleteGroupMemberAccount(
        parentGroupMemberData: GroupMemberData,
        targetGroupMemberData: GroupMemberData,
        callBack: (resultMessage: String) -> Unit
    ){
        CoroutineScope(Dispatchers.IO).launch {
            loadSubPathListsToDeleteGroupMemberAccount(parentGroupMemberData,targetGroupMemberData){pathLists, newPathLists ->
                val ref = Firebase.firestore.collection(GROUP_MEMBERS)
                Firebase.firestore.runTransaction {transaction->
                    for(pathList in pathLists){//삭제
                        for(data in pathList){
                            transaction.delete(ref.document(data.indexHashCode!!))
                        }
                    }
                    for(newPathList in newPathLists){//업로드
                        for(newData in newPathList){
                            transaction.set(ref.document(newData.indexHashCode!!), newData)
                        }
                    }
                    transaction.update(
                        ref.document(parentGroupMemberData.indexHashCode!!),
                        "childCount",
                        parentGroupMemberData.childCount - 1 // 자리 삭제했으니 자식이 하나 줄어든다
                    )
                }.addOnSuccessListener {
                        ref.document(GroupMemberData.getInstance().indexHashCode!!)// 혹시 자신의 하위유저가 지워진 경우 로컬에 갱신할 필요가 있음
                            .get()
                            .addOnSuccessListener {
                                GroupMemberData.setInstance(it.toObject<GroupMemberData>()!!)
                                callBack(RESULT_SUCCESS)
                            }

                    }
            }
        }
    }

    private suspend fun loadSubPathListsToDeleteGroupMemberAccount(
        parentGroupMemberData: GroupMemberData,
        targetGroupMemberData: GroupMemberData,
        callBack: (pathLists: MutableList<MutableList<GroupMemberData>>, newPathLists: MutableList<MutableList<GroupMemberData>>) -> Unit
    ){
        val pathLists = mutableListOf<MutableList<GroupMemberData>>()//   김 이 박 최
        val newPathLists = mutableListOf<MutableList<GroupMemberData>>()//김 박 최
        val mySubMembers = findSubGroupMemberListByIndex(parentGroupMemberData.indexHashCode!!)
        val newMySubMembers = findSubGroupMemberListByIndex(parentGroupMemberData.indexHashCode!!)
        newMySubMembers.remove(targetGroupMemberData)

        for(i in 0 until mySubMembers.size){
            val pathListPair = findAllSubGroupMemberData(mySubMembers[i], mGroupCode!!)
            pathLists.add(pathListPair)
        }

        for(i in 0 until newMySubMembers.size){
            val pathListPair = resetIndexHashCode(
                mySubMembers[i].indexHashCode!!, newMySubMembers[i], mGroupCode!!, mGroupCode!!
            )
            newPathLists.add(pathListPair)
        }

        callBack(pathLists, newPathLists)
    }

    suspend fun resetIndexHashCode(
        newHeadIndexHashCode: String,
        head: GroupMemberData,
        groupCode: String,
        newGroupCode: String
    ): MutableList<GroupMemberData>{
        val dataList = mutableListOf<GroupMemberData>()// 김중 김소(김병)
        val newDataList = mutableListOf<GroupMemberData>()// 김소(김병)
        dataList.add(head)

        val newHead = GroupMemberData()
        newHead.indexHashCode = newHeadIndexHashCode
        newHead.hashedGroupCode = head.hashedGroupCode
        newHead.admin = head.admin
        newHead.childCount = head.childCount
        newHead.id = head.id
        newDataList.add(newHead)

        val db = Firebase.firestore.collection(GROUP_MEMBERS)
        var index = 0
        var listSize = dataList.size
        while(listSize != index){
            val parent = dataList[index]
            val newParent = newDataList[index]
            val childCount = dataList[index].childCount
            for(i in 0 until childCount){
                val subIndexHashCode = hash(parent.indexHashCode+"!@#"+groupCode+"!@#"+ i)
                val subData = db.document(subIndexHashCode!!).get().await().toObject<GroupMemberData>()
                Log.d("asdnewsubData", parent.indexHashCode+"!@#"+groupCode+"!@#"+ i)
                dataList.add(subData!!)

                val newSubData = GroupMemberData()
                newSubData.indexHashCode = hash(newParent.indexHashCode+"!@#"+newGroupCode+"!@#"+ i)
                newSubData.hashedGroupCode = subData.hashedGroupCode
                newSubData.admin = subData.admin
                newSubData.childCount = subData.childCount
                newSubData.id = subData.id
                newDataList.add(newSubData)
                Log.d("asdnewnewSubData", newParent.indexHashCode+"!@#"+groupCode+"!@#"+ i)
            }
            index++
            listSize = dataList.size
        }
        return newDataList
    }

    suspend fun findAllSubGroupMemberData(head: GroupMemberData, groupCode: String): MutableList<GroupMemberData>{
        val dataList = mutableListOf<GroupMemberData>()
        dataList.add(head)
        val db = Firebase.firestore.collection(GROUP_MEMBERS)
        var index = 0
        var listSize = dataList.size
        while(listSize != index){
            val parent = dataList[index]
            val childCount = dataList[index].childCount
            for(i in 0 until childCount){
                val subIndexHashCode = hash(parent.indexHashCode+"!@#"+groupCode+"!@#"+ i)
                val subData = db.document(subIndexHashCode!!).get().await().toObject<GroupMemberData>()
                dataList.add(subData!!)
            }
            index++
            listSize = dataList.size
        }
        return  dataList
    }

    fun leaveGroup(indexHashCode: String, callBack: (resultMessage: String) -> Unit){
        // master계정에서의 탈퇴도 구현할 것
        val groupMemberData = hashMapOf<String, String?>(
            "hashedGroupCode" to null,
            "id" to null
        )
        Firebase.firestore.collection(GROUP_MEMBERS).document(indexHashCode)
            .set(groupMemberData, SetOptions.merge())
            .addOnSuccessListener {
                callBack(RESULT_SUCCESS)
            }
    }

    fun uploadUserData(userData: UserData, callBack: (message: String) -> Unit){
        userData.id?.let {
            Firebase.firestore.collection(USERS).document(it)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener {
                    callBack(RESULT_SUCCESS)
                }
        }
    }

    fun uploadGroupMemberData(groupMemberData: GroupMemberData, callBack: (message: String) -> Unit){
        groupMemberData.indexHashCode?.let {
            Firebase.firestore.collection(GROUP_MEMBERS).document(it)
                .set(groupMemberData, SetOptions.merge())
                .addOnSuccessListener {
                    callBack(RESULT_SUCCESS)
                }
        }
    }

    suspend fun findSubGroupMemberListByIndex(myIndexHashCode: String): MutableList<GroupMemberData>{
        val myGroupMemberData = Firebase.firestore.collection(GROUP_MEMBERS).document(myIndexHashCode)
            .get()
            .await()
            .toObject<GroupMemberData>()
        val childCount = myGroupMemberData!!.childCount
        val groupMemberList: MutableList<GroupMemberData> = mutableListOf()
        for (i in 0 until childCount){
            val childIndexHashCode: String? = hash(myGroupMemberData.indexHashCode+"!@#"+mGroupCode+"!@#"+i)
            val subGroupMemberData = Firebase.firestore.collection(GROUP_MEMBERS).document(childIndexHashCode!!)
                .get()
                .await()
                .toObject<GroupMemberData>()
            groupMemberList.add(subGroupMemberData!!)
        }
        return groupMemberList
    }

    suspend fun findSubUserListBySubGroupMemberList(groupMemberList: MutableList<GroupMemberData>): MutableList<UserData> {
        val userList: MutableList<UserData> = mutableListOf()

        for(groupMemberData in groupMemberList){
            if(groupMemberData.id.isNullOrEmpty()){
                userList.add(UserData())
            }
            else{
                val userData = Firebase.firestore.collection(USERS).document(groupMemberData.id!!)
                    .get()
                    .await()
                    .toObject<UserData>()
                userList.add(userData!!)
            }
        }
        return userList
    }

    fun checkGroupCodeValid(id: String?, groupCode: String?, hashedGroupCode: String?): Boolean{
        if (AccountManager().hash(id + "!@#" + groupCode) == hashedGroupCode){
            mGroupCode = groupCode
            return true
        }
        else if (AccountManager().hash(id + "!@#" + groupCode + "!@#" + "master") == hashedGroupCode){
            mGroupCode = groupCode
            mMaster = true
            return true
        }
        else
            return false
    }

    fun inviteSubUser(parentGroupMemberData: GroupMemberData, subUserId: String, isAdmin: Boolean, callBack: (resultMessage: String) -> Unit){
        findUserDataById(subUserId){ resultMessage1, querySnapShot ->
            if(resultMessage1 == RESULT_FAILURE){//초대할 아이디 존재안함
                callBack(ERROR_NOT_FOUND_ID)
            }
            else if(resultMessage1 == RESULT_SUCCESS){//아이디 있음

                findGroupMemberDataById(subUserId){resultMessage2, querySnapShot ->
                    if(resultMessage2 == RESULT_SUCCESS){
                        callBack(ERROR_GROUPED_ID)
                    }
                    else if(resultMessage2 == RESULT_FAILURE){
                        val myGroupMemberData = parentGroupMemberData
                        val subHashedGroupCode = hash(subUserId+"!@#"+ mGroupCode)
                        val subIndexHashCode = hash(myGroupMemberData.indexHashCode+"!@#"+mGroupCode+"!@#"+ myGroupMemberData.childCount)
                        val subGroupMemberData = GroupMemberData()

                        subGroupMemberData.id = subUserId
                        subGroupMemberData.indexHashCode = subIndexHashCode
                        subGroupMemberData.hashedGroupCode = subHashedGroupCode
                        subGroupMemberData.childCount = 0
                        subGroupMemberData.admin = isAdmin

                        Firebase.firestore.runTransaction {
                            it.set(
                                Firebase.firestore.collection(GROUP_MEMBERS).document(subIndexHashCode!!),
                                subGroupMemberData
                            )
                            it.update(
                                Firebase.firestore.collection(GROUP_MEMBERS).document(myGroupMemberData.indexHashCode!!),
                                "childCount",
                                myGroupMemberData.childCount + 1
                            )
                        }.addOnSuccessListener {
                            myGroupMemberData.childCount += 1
                            callBack(RESULT_SUCCESS)
                        }
                    }
                }
            }
        }
    }

    fun fillEmptyAccount(
        childGroupMemberData: GroupMemberData,
        subUserId: String, isAdmin: Boolean,
        callBack: (resultMessage: String) -> Unit
    ){
        findUserDataById(subUserId){ resultMessage1, querySnapShot ->
            if(resultMessage1 == RESULT_FAILURE){//초대할 아이디 존재안함
                callBack(ERROR_NOT_FOUND_ID)
            }
            else if(resultMessage1 == RESULT_SUCCESS){//아이디 있음

                findGroupMemberDataById(subUserId){resultMessage2, querySnapShot ->
                    if(resultMessage2 == RESULT_SUCCESS){
                        callBack(ERROR_GROUPED_ID)
                    }
                    else if(resultMessage2 == RESULT_FAILURE){
                        val subHashedGroupCode = hash(subUserId+"!@#"+ mGroupCode)
                        val subIndexHashCode = childGroupMemberData.indexHashCode
                        val subGroupMemberData = childGroupMemberData

                        subGroupMemberData.id = subUserId
                        subGroupMemberData.hashedGroupCode = subHashedGroupCode
                        subGroupMemberData.admin = isAdmin

                        Firebase.firestore.runTransaction {
                            it.set(
                                Firebase.firestore.collection(GROUP_MEMBERS).document(subIndexHashCode!!),
                                subGroupMemberData
                            )
                        }.addOnSuccessListener {
                            callBack(RESULT_SUCCESS)
                        }
                    }
                }
            }
        }
    }

    fun publishGroup(
        context: Context,
        groupCode: String,
        callBack: (resultMessage: String) -> Unit
    ){
        val userData = UserData.getInstance()
        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }
        val hashedMasterGroupCode = hash(userData.id+"!@#"+groupCode+"!@#"+"master")
        val indexHashCode = hash(userData.id+"!@#"+groupCode)
        val groupMemberData = GroupMemberData()

        groupMemberData.id = userData.id
        groupMemberData.indexHashCode = indexHashCode
        groupMemberData.hashedGroupCode = hashedMasterGroupCode
        groupMemberData.childCount = 0
        groupMemberData.admin = true

        Firebase.firestore.collection(GROUP_MEMBERS).document(indexHashCode!!)
            .set(groupMemberData)
            .addOnSuccessListener {

                GroupMemberData.setInstance(groupMemberData)

                callBack(RESULT_SUCCESS)
            }
    }

    fun signIn(context: Context,userData: UserData, callBack: (resultMessage: String) -> Unit){
        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }
        Firebase.firestore.collection(USERS).document(userData.id!!)
            .set(userData)
            .addOnSuccessListener {
                callBack(RESULT_SUCCESS)
            }
    }

    fun login(id: String, pw: String, callBack: (resultMessage: String) -> Unit){
        findUserDataById(id){resultMessage, querySnapShot ->
            if(resultMessage == RESULT_FAILURE)
                callBack(ERROR_NOT_FOUND_ID)// 아이디가 존재하지 않음
            else if(resultMessage == RESULT_SUCCESS){
                val doc = querySnapShot.documents[0]
                val userData = doc.toObject<UserData>()
                if (userData != null) {
                    if(userData.pw != pw){
                        callBack(ERROR_WRONG_PASSWORD)
                        return@findUserDataById
                    }// 비밀번호가 달라 실패
                    userData.login = true //접속중으로 처리
                    UserData.setInstance(userData) // 서버에서 받은 계정정보를 등록
                    Firebase.firestore.collection(USERS).document(userData.id!!)
                        .set(userData) // 서버에 접속중임을 알림
                    Firebase.firestore.collection(GROUP_MEMBERS).whereEqualTo("id",userData.id)
                        .get()
                        .addOnSuccessListener {
                            if(it.isEmpty){
                                callBack(RESULT_SUCCESS)
                            }
                            else{
                                it.documents[0].toObject<GroupMemberData>()?.let { groupMemberData ->
                                    GroupMemberData.setInstance(
                                        groupMemberData
                                    )
                                }
                                callBack(RESULT_SUCCESS)
                            }
                        }
                }
            }
        }
    }

    fun logout(){
        val userData = UserData.getInstance()
        userData.login = false
        GroupMemberData.setInstance(GroupMemberData())// 초기화
        Firebase.firestore.collection(USERS).document(userData.id!!)
            .set(userData) // 서버에 로그아웃함을 알림
    }

    fun autoLogin(context: Context, callBack: (resultMessage: String) -> Unit){
        val pm = PreferenceManager()
        if(pm.isAutoLoginEnable(context) && !UserData.getInstance().login){
            val loginData = pm.getLoginData(context)
            mGroupCode = pm.getGroupCode(context)
            login(loginData[0].toString(), loginData[1].toString()){resultMessage ->
                if(resultMessage == RESULT_SUCCESS)
                    callBack(RESULT_SUCCESS)
                else
                    callBack(RESULT_FAILURE)
            }
        }
        else{
            callBack(RESULT_FAILURE)
        }
    }

    fun checkIfIdIsDuplicate(id: String, callBack: (resultMessage: String, querySnapShot: QuerySnapshot) -> Unit){
        findUserDataById(id){resultMessage, querySnapShot ->
            if(resultMessage == RESULT_FAILURE){
                callBack(RESULT_SUCCESS, querySnapShot)
            }
            else{
                callBack(ERROR_DUPLICATE_ID, querySnapShot)
            }
        }
    }

    fun findUserDataById(id: String, callBack: (resultMessage: String, querySnapShot: QuerySnapshot) -> Unit){
        Firebase.firestore.collection(USERS).whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    callBack(RESULT_FAILURE, it)
                }
                else{
                    callBack(RESULT_SUCCESS, it)
                }
            }
    }

    fun loadGroupMemberDataById(id: String, callBack: (resultMessage: String) -> Unit){
        findGroupMemberDataById(id){resultMessage, querySnapShot ->
            if(resultMessage == RESULT_SUCCESS){
                val groupMemberData = querySnapShot.documents[0].toObject<GroupMemberData>()

                if (groupMemberData != null) {
                    GroupMemberData.setInstance(groupMemberData)
                }

                callBack(RESULT_SUCCESS)
            }else{
                callBack(RESULT_FAILURE)
            }
        }
    }

    fun findGroupMemberDataById(id: String, callBack: (resultMessage: String, querySnapShot: QuerySnapshot) -> Unit){
        Firebase.firestore.collection(GROUP_MEMBERS).whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    callBack(RESULT_FAILURE, it)
                }
                else{
                    callBack(RESULT_SUCCESS, it)
                }
            }
    }

    fun hash(text: String): String? {
        val sha = SHA256()
        return sha.encrypt(text)

//        confirmCode(hashedGroupCode): id+"!@#"+그룹코드, id+"!@#"+그룹코드+"!@#"+"master"(둘중하나면 그룹코드 유효 인정)
//        최초인덱스해시: id+"!@#"+그룹코드
//        그이후인덱스해시: 부모인덱스해시+"!@#"+그룹코드+"!@#"+childCount
//        초대해시코드: 부모id+"!@#"+그룹코드+"!@#"+초대코드
    }

    fun checkNetworkState(context: Context): Boolean {//인터넷 상태를 확인하는 함수
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    companion object{
        var mGroupCode: String? = null //트리순회에 필요하므로 로그인 시 정적으로 입력할 것
        var mMaster: Boolean = false

        const val TAG = "AccountManager"
        const val USERS = "users"
        const val GROUP_MEMBERS = "group members"
        const val ERROR_NOT_FOUND_ID = "아이디없는 오류"
        const val ERROR_DUPLICATE_ID = "아이디 중복 오류"
        const val ERROR_GROUPED_ID = "이미 그룹에 속한 아이디"
        const val ERROR_WRONG_INFO = "입력정보가 다름"
        const val ERROR_WRONG_PASSWORD = "비밀번호가 다름"
        const val ERROR_NETWORK_NOT_CONNECTED = "네트워크 연결 안 됨"
        const val RESULT_SUCCESS = "성공"
        const val RESULT_FAILURE = "실패"

    }
}

class SHA256(){
    fun encrypt(text: String): String? {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(text.toByteArray(Charset.defaultCharset()))

        return bytesToHex(md.digest())
    }

    private fun bytesToHex(bytes: ByteArray): String? {
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }
}
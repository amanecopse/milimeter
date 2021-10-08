package com.amnapp.milimeter

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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

    fun resetGroupCode(context: Context, newGroupCode: String, callBack: (resultMessage: String) -> Unit){
        val myGroupMemberData = GroupMemberData.getInstance()

        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val newGroupMemberDataList = resetIndexHashCode(
                myGroupMemberData.indexHashCode!!,
                myGroupMemberData,
                mGroupCode!!,
                newGroupCode,
                true
            )
            val oldGroupMemberDataList = findAllSubGroupMemberData(myGroupMemberData, mGroupCode!!)

            val ref = Firebase.firestore.collection(GROUP_MEMBERS)
            Firebase.firestore.runTransaction { transaction->
                for(oldSubGroupMemberData in oldGroupMemberDataList){//삭제
                    transaction.delete(ref.document(oldSubGroupMemberData.indexHashCode!!))
                }
                for(newSubGroupMemberData in newGroupMemberDataList){//업로드
                    transaction.set(ref.document(newSubGroupMemberData.indexHashCode!!), newSubGroupMemberData)
                }
            }.addOnSuccessListener {
                Firebase.firestore.collection(GROUP_MEMBERS).document(GroupMemberData.getInstance().indexHashCode!!)
                    .get()
                    .addOnSuccessListener {
                        GroupMemberData.setInstance(it.toObject<GroupMemberData>()!!)
                        mGroupCode = newGroupCode
                        callBack(RESULT_SUCCESS)
                    }
            }
        }
    }

    fun mergeSubGroup(context: Context, subId: String, subPw: String, subGroupCode: String, callBack: (resultMessage: String) -> Unit){

        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            var valid = true
            val myGroupMemberData = GroupMemberData.getInstance()
            val groupMemberDocs =
                Firebase.firestore.collection(GROUP_MEMBERS).whereEqualTo("id", subId)
                    .get()
                    .addOnSuccessListener {
                        if(it.isEmpty){
                            callBack(ERROR_NOT_FOUND_ID)// 그룹 가입한 아이디 존재하지않음, 혹은 아이디 자체가 없음
                            valid = false
                        }
                    }
                    .await()
            if(!valid) return@launch
            val headGroupMemberData = groupMemberDocs.documents[0].toObject<GroupMemberData>()
            Firebase.firestore.collection(USERS).document(subId)
                .get()
                .addOnSuccessListener {
                    val headUserData = it.toObject<UserData>()
                    if(headGroupMemberData == null || it.toObject<UserData>() == null){
                        callBack(ERROR_NOT_FOUND_ID)// 그룹 가입한 아이디 존재하지않음, 혹은 아이디 자체가 없음
                        valid = false
                    }
                    else if(headUserData!!.pw != subPw){//서브의 비밀번호 불일치
                        callBack(ERROR_WRONG_PASSWORD)
                        valid = false
                    }
                    else if(headGroupMemberData.hashedGroupCode != hash(headGroupMemberData.id+"!@#"+ subGroupCode +"!@#"+"master")){
                        callBack(ERROR_WRONG_INFO)// 서브그룹코드가 다르거나 Master가 아님
                        valid = false
                    }
                }
                .await()
                .toObject<UserData>()
            if(!valid) return@launch
            val oldSubGroupMemberList = findAllSubGroupMemberData(headGroupMemberData!!, subGroupCode)
            val newSubGroupMemberList = resetIndexHashCode(
                hash(myGroupMemberData.indexHashCode+"!@#"+ mGroupCode+"!@#"+myGroupMemberData.childCount)!!,
                headGroupMemberData,
                subGroupCode,
                mGroupCode!!,
                false
            )
            val ref = Firebase.firestore.collection(GROUP_MEMBERS)
            Firebase.firestore.runTransaction { transaction->
                for(oldSubGroupMemberData in oldSubGroupMemberList){//삭제
                    transaction.delete(ref.document(oldSubGroupMemberData.indexHashCode!!))
                }
                for(newSubGroupMemberData in newSubGroupMemberList){//업로드
                    transaction.set(ref.document(newSubGroupMemberData.indexHashCode!!), newSubGroupMemberData)
                }
                transaction.update(
                    ref.document(myGroupMemberData.indexHashCode!!),
                    "childCount",
                    myGroupMemberData.childCount + 1 // 서브그룹의 그룹장이 하위로 추가됐으니 1증가
                )
            }.addOnSuccessListener {
                myGroupMemberData.childCount += 1 //로컬에 갱신
                callBack(RESULT_SUCCESS)
            }.addOnFailureListener{
                callBack(RESULT_FAILURE)
            }
        }
    }

    fun deleteGroupMemberAccount(// loadSubPathListsToDeleteGroupMemberAccount에서 변화전과 변화후의 리스트를 로드하면 이 함수에서 삭제,업로드 작업을 진행한다
        context: Context,
        parentGroupMemberData: GroupMemberData,
        targetGroupMemberData: GroupMemberData,
        callBack: (resultMessage: String) -> Unit
    ){

        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }

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
                        ref.document(GroupMemberData.getInstance().indexHashCode!!)// 혹시 로그인한 자신의 하위유저가 지워진 경우 바로 로컬에 갱신할 필요가 있음
                            .get()
                            .addOnSuccessListener {
                                GroupMemberData.setInstance(it.toObject<GroupMemberData>()!!)
                                callBack(RESULT_SUCCESS)
                            }

                    }
            }
        }
    }

    private suspend fun loadSubPathListsToDeleteGroupMemberAccount(// 공석을 삭제하기 위해 기존 트리데이터와 삭제후 트리데이터를 로드하는 함수
        parentGroupMemberData: GroupMemberData,// 공석의 부모인 그룹멤버데이터
        targetGroupMemberData: GroupMemberData,// 공석의 그룹멤버데이터
        callBack: (pathLists: MutableList<MutableList<GroupMemberData>>, newPathLists: MutableList<MutableList<GroupMemberData>>) -> Unit
    ){
        val pathLists = mutableListOf<MutableList<GroupMemberData>>()//기존 트리가 리스트화된 것, 후에 순차적으로 삭제됨
        val newPathLists = mutableListOf<MutableList<GroupMemberData>>()//변화가 적용된 새 트리가 리스트화된 것, 후에 순차적으로 업로드됨
        val mySubMembers = findSubGroupMemberListByIndex(parentGroupMemberData.indexHashCode!!)// 부모의 바로아래 하위유저들
        val newMySubMembers = findSubGroupMemberListByIndex(parentGroupMemberData.indexHashCode!!)// 부모의 공석을 제외한 바로아래 하위유저들(아래코드에서 제거됨)
        newMySubMembers.remove(targetGroupMemberData)// 공석을 제외

        for(i in 0 until mySubMembers.size){
            val pathListPair = findAllSubGroupMemberData(mySubMembers[i], mGroupCode!!)
            pathLists.add(pathListPair)
        }

        for(i in 0 until newMySubMembers.size){
            val pathListPair = resetIndexHashCode(
                mySubMembers[i].indexHashCode!!, newMySubMembers[i], mGroupCode!!, mGroupCode!!, false
            )
            newPathLists.add(pathListPair)
        }

        callBack(pathLists, newPathLists)
    }

    private suspend fun resetIndexHashCode(//그룹의 그룹코드나 그룹장의 인덱스해시코드를 재설정하는 함수 Master여부를 받아야함
        newHeadIndexHashCode: String,// 그룹장의 새로운 인덱스해시코드
        head: GroupMemberData,//그룹장의 기존 데이터
        groupCode: String,//기존 그룹코드
        newGroupCode: String,//새 그룹코드
        master: Boolean
    ): MutableList<GroupMemberData>{
        val dataList = mutableListOf<GroupMemberData>()// 기존 트리의 리스트화
        val newDataList = mutableListOf<GroupMemberData>()// 변화가 적용된 트리의 리스트화
        dataList.add(head)

        val newHead = GroupMemberData()
        newHead.indexHashCode = newHeadIndexHashCode
        newHead.hashedGroupCode =
            if(master) hash(head.id+"!@#"+ newGroupCode +"!@#"+"master")
            else hash(head.id+"!@#"+ newGroupCode)
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
                if (subData != null) {
                    dataList.add(subData)
                }
                else
                    break

                val newSubData = GroupMemberData()
                newSubData.indexHashCode = hash(newParent.indexHashCode+"!@#"+newGroupCode+"!@#"+ i)
                newSubData.hashedGroupCode = hash(subData.id+"!@#"+ newGroupCode)
                newSubData.admin = subData.admin
                newSubData.childCount = subData.childCount
                newSubData.id = subData.id
                newDataList.add(newSubData)
            }
            index++
            listSize = dataList.size
        }
        return newDataList
    }

    suspend fun findAllSubGroupMemberData(head: GroupMemberData, groupCode: String): MutableList<GroupMemberData>{//head를 포함하는 모든 트리 구성원을 리스트화하여 리턴
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
                if (subData != null) {
                    dataList.add(subData)
                }
            }
            index++
            listSize = dataList.size
        }
        return  dataList
    }

    fun leaveGroup(context: Context, myIndexHashCode: String, master: Boolean, callBack: (resultMessage: String) -> Unit){

        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }

        if(master){//master권한자의 그룹탈퇴, 공석없이 자리까지 없어지며 master권한을 바로 밑 하위유저에게 위임
            CoroutineScope(Dispatchers.IO).launch {

                var valid = true
                val subGroupMemberDatas = findSubGroupMemberListByIndex(myIndexHashCode)
                val ref = Firebase.firestore.collection(GROUP_MEMBERS)

                ref.document(myIndexHashCode)// 코투틴에서 다이얼로그콜백을 못써서 사용하는 의미없는 쿼리
                    .get()
                    .addOnSuccessListener {
                        for(subGroupMemberData in subGroupMemberDatas){// 바로 밑 하위유저중 공란이 있는지 확인, 있으면 탈퇴불가
                            if(subGroupMemberData.id == null) {
                                valid = false
                                callBack(RESULT_FAILURE)
                            }
                        }
                    }
                    .await()
                if(!valid) return@launch

                Firebase.firestore.runTransaction { transaction ->
                    for(subGroupMemberData in subGroupMemberDatas){
                        val newHashedMasterGroupCode = hash(subGroupMemberData.id+"!@#"+ mGroupCode +"!@#"+"master")
                        transaction.update(
                            ref.document(subGroupMemberData.indexHashCode!!),
                            "hashedGroupCode",
                            newHashedMasterGroupCode)
                        transaction.update(
                            ref.document(subGroupMemberData.indexHashCode!!),
                            "admin",
                            true)
                    }
                    transaction.delete(ref.document(myIndexHashCode))
                }.addOnSuccessListener {
                    GroupMemberData.setInstance(GroupMemberData())// 탈퇴했으니 로컬에서 그룹정보 초기화
                    callBack(RESULT_SUCCESS)
                }
            }
        }
        else{// 관리자나 일반일 경우
            val groupMemberData = hashMapOf<String, String?>(
                "hashedGroupCode" to null,
                "id" to null
            )
            Firebase.firestore.collection(GROUP_MEMBERS).document(myIndexHashCode)
                .set(groupMemberData, SetOptions.merge())
                .addOnSuccessListener {
                    if(myIndexHashCode == GroupMemberData.getInstance().indexHashCode){// 본인탈퇴인 경우
                        GroupMemberData.setInstance(GroupMemberData())// 탈퇴했으니 로컬에서 그룹정보 초기화
                        callBack(RESULT_SUCCESS)
                    }
                    else{// 관리자창에서 다른 하위유저를 탈퇴시키는 경우
                        callBack(RESULT_SUCCESS)
                    }
                }
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

    suspend fun findSubGroupMemberListByIndex(myIndexHashCode: String): MutableList<GroupMemberData>{//부모인덱스해시코드로 자식그룹멤버데이터를 찾음
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
            if (subGroupMemberData != null) {
                groupMemberList.add(subGroupMemberData)
            }
        }
        return groupMemberList
    }

    suspend fun findSubUserListBySubGroupMemberList(groupMemberList: MutableList<GroupMemberData>): MutableList<UserData> {// 그룹멤버리스트를 유저리스트로 변환
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

    fun checkGroupCodeValid(context: Context, id: String?, groupCode: String?, hashedGroupCode: String?): Boolean{//그룹코드 유효성을 체크하고 여부를 리턴, Master권한 여부에 따라 mMaster 설정
        if (AccountManager().hash(id + "!@#" + groupCode) == hashedGroupCode){
            mGroupCode = groupCode
            mMaster = false
            return true
        }
        else if (AccountManager().hash(id + "!@#" + groupCode + "!@#" + "master") == hashedGroupCode){
            mGroupCode = groupCode
            mMaster = true
            Toast.makeText(context, "Master 권한 계정입니다", Toast.LENGTH_LONG).show()
            return true
        }
        else
            return false
    }

    fun inviteSubUser(context: Context, parentGroupMemberData: GroupMemberData, subUserId: String, isAdmin: Boolean, callBack: (resultMessage: String) -> Unit){

        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }

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
        context: Context,
        childGroupMemberData: GroupMemberData,
        subUserId: String, isAdmin: Boolean,
        callBack: (resultMessage: String) -> Unit
    ){

        if(!checkNetworkState(context)){
            callBack(ERROR_NETWORK_NOT_CONNECTED)
            return
        }

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
        val indexHashCode = hash(userData.id+"!@#"+groupCode+"!@#"+Math.random().toString())
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

    fun login(context: Context, id: String, pw: String, callBack: (resultMessage: String) -> Unit){
        val am = AccountManager()
        val pm = PreferenceManager()
        val gc = pm.getGroupCode(context)
        am.findGroupMemberDataById(id.toString()){resultMessage, querySnapShot ->//그룹멤버데이터에서 hashedGroupCode를 가져옴
            if(resultMessage == RESULT_SUCCESS) {
                val groupMemberData = querySnapShot.documents[0].toObject<GroupMemberData>()
                if(checkGroupCodeValid(context, id, gc, groupMemberData!!.hashedGroupCode))//hashedGroupCode와 로그인 캐시를 대조
                    mGroupCode = gc// 대조해서 그룹코드가 유효하면 mGroupCode에 입력
                else
                    mGroupCode = null //유효하지 않으면 null
            }

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
        val userData = UserData.getInstance()
        if(pm.isAutoLoginEnable(context) && !userData.login){
            val loginData = pm.getLoginData(context)
            val id = loginData[0]
            val pw = loginData[1]
            login(context, id.toString(), pw.toString()){resultMessage2 ->//로그인 캐시로 로그인 시도
                if(resultMessage2 == RESULT_SUCCESS)
                    callBack(RESULT_SUCCESS)
                else
                    callBack(RESULT_FAILURE)
            }
        }
        else{
            callBack(RESULT_FAILURE)
        }
    }

    fun checkIfIdIsDuplicate(context: Context, id: String, callBack: (resultMessage: String, querySnapShot: QuerySnapshot) -> Unit){

        findUserDataById(id){resultMessage, querySnapShot ->

            if(!checkNetworkState(context)){
                callBack(ERROR_NETWORK_NOT_CONNECTED, querySnapShot)
                return@findUserDataById
            }

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
//        최초인덱스해시: id+"!@#"+그룹코드+"!@#"+난수
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
        var mGroupCode: String? = null //트리순회에 필요하므로 로그인 시 입력할 것
        var mMaster: Boolean = false//그룹코드 유효성 체크 시 master권한 여부도 판별되므로 그떄 같이 입력할 것
        var mStartScreen = true// 첫화면일 때까지 true로 유지하다가 자동로그인 함수 실행하면서 false입력(자동로그인을 첫화면에서 한 번만 실행하기 위함)

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
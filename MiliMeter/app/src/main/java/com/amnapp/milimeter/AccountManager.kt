package com.amnapp.milimeter

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amnapp.milimeter.activities.InviteCodeIssueActivity
import com.amnapp.milimeter.activities.SignInActivity
import com.amnapp.milimeter.activities.LoginActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.nio.charset.Charset
import java.security.MessageDigest

class AccountManager {
    var db = Firebase.firestore

    fun signInWithoutInvite(// 그룹개설자 가입과정
        context: Context,
        id: String,
        pw: String,
        groupCode: String,
        userName: String,
        militaryId: Int,
        userHeight: Int,
        userWeight: Int,

        userAge: Int? = null,
        userBloodType: Int? = null,

        goalOfWeight: Int? = null,
        goalOfTotalRank: Int? = null,
        goalOfLegTuckRank: Int? = null,
        goalOfShuttleRunRank: Int? = null,
        goalOfFieldTrainingRank: Int? = null
    ){

        val activity = context as SignInActivity
        activity.binding.confirmLL.isClickable = false //연타방지

        val confirmHashCode = hash(id+"!@#"+pw+"!@#"+groupCode)
        val ud = UserData() //getInstance()로 얻어낼 경우 이전 로그인 객체를 불러올 위험이 있으므로 빈 객체 생성
        var isValid: Boolean = true
        ud.id = id
        ud.pw = pw
        ud.userName = userName
        ud.userHeight = userHeight
        ud.userWeight = userWeight
        ud.confirmHashCode = confirmHashCode
        ud.isAdmin = true

        ud.userAge = userAge
        ud.militaryId = militaryId
        ud.userBloodType = userBloodType
        ud.goalOfWeight = goalOfWeight
        ud.goalOfTotalRank = goalOfTotalRank
        ud.goalOfLegTuckRank = goalOfLegTuckRank
        ud.goalOfShuttleRunRank = goalOfShuttleRunRank
        ud.goalOfFieldTrainingRank = goalOfFieldTrainingRank

        hash(id+pw+userName)?.let {
            ud.indexHashCode = it
            db.collection("users").document(it)
                .set(ud)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    Toast.makeText(context, "가입성공", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
                .addOnFailureListener {e ->
                    Log.w(TAG, "Error writing document", e)
                    Toast.makeText(context, "가입실패", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
        }
    }

    fun signInWithInvite(
        context: Context,
        id: String,
        pw: String,
        groupCode: String,
        hostId: String,
        inviteCode: String,
        userName: String,
        militaryId: Int,
        userHeight: Int,
        userWeight: Int,

        userAge: Int? = null,
        userBloodType: Int? = null,

        goalOfWeight: Int? = null,
        goalOfTotalRank: Int? = null,
        goalOfLegTuckRank: Int? = null,
        goalOfShuttleRunRank: Int? = null,
        goalOfFieldTrainingRank: Int? = null
    ) {

        val activity = context as SignInActivity
        activity.binding.confirmLL.isClickable = false //연타방지

        val inviteHashCode = hash(hostId+"!@#"+inviteCode+"!@#"+groupCode)
        val confirmHashCode = hash(id+"!@#"+pw+"!@#"+groupCode)
        db.collection("users").whereEqualTo("inviteHashCode",inviteHashCode)
            .get()
            .addOnSuccessListener { querySnapshot ->

                if(querySnapshot.isEmpty){// 초대해시코드로 못찾음 -> 초대자id,초대코드,그룹코드 3중하나 잘못입력
                    val activity = context as SignInActivity
                    activity.showDialogMessage("초대자 정보가 유효하지 않습니다", "초대코드, 초대자 id 또는 그룹코드가 잘못 입력 되었습니다")
                    activity.binding.confirmLL.isClickable = true //연타방지해제
                    return@addOnSuccessListener
                }

                val newUd = querySnapshot.documents[0].toObject<UserData>()
                if (newUd != null) {
                    newUd.id = id
                    newUd.pw = pw
                    newUd.confirmHashCode = confirmHashCode
                    newUd.inviteHashCode = null
                    newUd.userName = userName
                    newUd.userHeight = userHeight
                    newUd.userWeight = userWeight
                    newUd.userAge = userAge
                    newUd.militaryId = militaryId
                    newUd.userBloodType = userBloodType
                    newUd.goalOfWeight = goalOfWeight
                    newUd.goalOfTotalRank = goalOfTotalRank
                    newUd.goalOfLegTuckRank = goalOfLegTuckRank
                    newUd.goalOfShuttleRunRank = goalOfShuttleRunRank
                    newUd.goalOfFieldTrainingRank = goalOfFieldTrainingRank

                    db.collection("users").document(newUd.indexHashCode.toString())
                        .set(newUd)
                        .addOnSuccessListener {
                            db.collection("users").whereEqualTo("id",hostId)
                                .get()
                                .addOnSuccessListener {
                                    val hostUd = it.documents[0].toObject<UserData>()
                                    if (hostUd != null) {
                                        hostUd.inviteHashCode = null //부모의 초대코드 발급 상태를 초기화시킨다
                                        db.collection("users").document(hostUd.indexHashCode.toString())
                                            .set(hostUd)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "가입성공", Toast.LENGTH_SHORT).show()
                                                val activity = context as SignInActivity
                                                activity.finish()
                                            }
                                    }
                                }
                        }
                }

            }
    }

    fun login(context: Context, id: String, pw: String, groupCode: String){
        val activity: LoginActivity = context as LoginActivity
        activity.binding.loginLl.isClickable = false //연타 방지
        activity.mDialog.show() // 로딩화면 실행
        db.collection("users").whereEqualTo("id",id)
            .get().addOnSuccessListener {
                if(it.isEmpty){
                    activity.showDialogMessage("로그인 실패", "존재하지 않는 아이디 입니다")
                }
                else{
                    val document = it.documents[0]
                    val ud= document.toObject<UserData>()
                    val confirmHashCode = hash(id+"!@#"+pw+"!@#"+groupCode)
                    if (ud != null) {
                        if(ud.confirmHashCode==confirmHashCode){
                            ud.isLogined = true
                            UserData.setInstance(ud) // 서버에서 얻은 객체로 대체
                            mGroupCode = groupCode // 그룹코드 저장

                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                            activity.binding.profileCiv.visibility = View.VISIBLE
                            activity.binding.loginLl.visibility = View.GONE
                            activity.binding.signInCv.visibility = View.GONE
                            activity.binding.loginBoxCv.visibility = View.GONE
                            activity.binding.logoutLl.visibility = View.VISIBLE
                            activity.binding.issueCv.visibility = View.VISIBLE
                            activity.binding.adminCv.visibility = if(ud.isAdmin) View.VISIBLE else View.GONE
                            activity.binding.loginoutCv.setCardBackgroundColor(Color.RED)
                        }
                        else{
                            activity.showDialogMessage("로그인 실패", "비밀번호 또는 그룹코드가 다릅니다")
                        }

                    }
                }
                activity.binding.loginLl.isClickable = true //연타방지 해제
                activity.mDialog.dismiss() // 로딩해제
            }
    }

    fun logout(context: Context){
        val activity = context as LoginActivity
        val ud = UserData.getInstance()
        activity.binding.profileCiv.visibility = View.GONE
        activity.binding.loginLl.visibility = View.VISIBLE
        activity.binding.signInCv.visibility = View.VISIBLE
        activity.binding.loginBoxCv.visibility = View.VISIBLE
        activity.binding.logoutLl.visibility = View.GONE
        activity.binding.issueCv.visibility = View.GONE
        activity.binding.adminCv.visibility = View.GONE
        activity.binding.loginoutCv.setCardBackgroundColor(Color.WHITE)

        ud.isLogined = false

    }

    fun issueInviteCode(context: Context, inviteCode: String, isAdmin: Boolean){
        val activity = context as InviteCodeIssueActivity
        val myUd = UserData.getInstance()
        val childUd = UserData() // 빈 객체 생성
        val inviteHashCode = hash(myUd.id+"!@#"+inviteCode+"!@#"+mGroupCode)
        val childIndexHashCode = hash(myUd.indexHashCode+"!@#"+mGroupCode+"!@#"+myUd.childCount)
        val myIndexHashCode = myUd.indexHashCode

        if (childIndexHashCode != null && inviteHashCode != null && myIndexHashCode != null) {
            childUd.isAdmin = isAdmin
            childUd.inviteHashCode = inviteHashCode
            childUd.indexHashCode = childIndexHashCode
            childUd.userName = "초대중인 계정"
            myUd.inviteHashCode = inviteCode// 초대코드 발급중인 부모계정임을 알리면서 현재 발급중인 코드(해시가 아닌 형태)를 저장

            //발급한 현재 상태를 서버에 올림
            db.collection("users").document(childIndexHashCode)
                .set(childUd)
                .addOnSuccessListener {
                    myUd.childCount += 1 //부모의 자식 카운트를 1늘려준다
                    db.collection("users").document(myIndexHashCode)
                        .set(myUd)
                        .addOnSuccessListener {
                            activity.showDialogMessage("초대코드 발급 완료", "초대할 유저에게 초대코드를 공유해 주세요")
                        }
                }
        }
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

    suspend fun findChildAccount(myIndexHashCode: String): MutableList<UserData>{
        val userList: MutableList<UserData> = mutableListOf()
        val childCount: Int
        val myUd = db.collection("users").document(myIndexHashCode).get().await().toObject<UserData>()
        if (myUd != null) {
            childCount = myUd.childCount
            for (i in 0 until childCount){
                val childUd: UserData?
                val childIndexHashCode: String? = hash(myIndexHashCode+"!@#"+mGroupCode+"!@#"+i)
                childUd =
                    childIndexHashCode?.let { db.collection("users").document(it).get().await().toObject<UserData>() }
                if (childUd != null) {
                    userList.add(childUd)
                }
            }
        }

        return userList
    }

    fun hash(text: String): String? {
        val sha = SHA256()
        return sha.encrypt(text)

        //초대해시코드는 myUd.id+"!@#"+inviteCode+"!@#"+mGroupCode
        //자식의 인덱스 새로 만들 때는 -> myIndexHashCode+"!@#"+mGroupCode+"!@#"+i 순서유의
    }

    companion object{
        var mGroupCode: String? = null //트리순회에 필요하므로 로그인 시 정적으로 입력할 것
        const val TAG = "AccountManager"
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
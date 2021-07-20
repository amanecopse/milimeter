package com.amnapp.milimeter.activities

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.databinding.ActivitySignInBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {

        //초대받은 회원의 가입 시 보이는 UI의 토글
        binding.inviteInfoCv.visibility = View.GONE
        binding.isInvitedCb.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> binding.inviteInfoCv.visibility = View.VISIBLE
                false -> binding.inviteInfoCv.visibility = View.GONE
            }
        }

        //회원가입 과정
        binding.confirmLL.setOnClickListener {

            val am = AccountManager()
            if(!am.checkNetworkState(this)){
                showDialogMessage("네트워크 오류", "네트워크 연결 상태를 확인해 주세요")
                return@setOnClickListener
            }

            db.collection("users").whereEqualTo("id",binding.idEt.text.toString()) //아이디중복체크
                .get()
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        showDialogMessage("중복된 아이디 입니다", "다른 아이디로 입력해 주세요")
                    }
                    else{//아이디중복체크 통과
                        var isValid = !(binding.pwEt.text.isNullOrBlank()// 공란 검사
                                || binding.pwConfirmEt.text.isNullOrBlank()
                                || binding.groupCodeEt.text.isNullOrBlank()
                                || binding.nameEt.text.isNullOrBlank()
                                || binding.militaryIdEt.text.isNullOrBlank()
                                || binding.heightEt.text.isNullOrBlank()
                                || binding.weightEt.text.isNullOrBlank()
                                || binding.pwEt.text.toString() != binding.pwConfirmEt.text.toString())

                        if(binding.isInvitedCb.isChecked){// 체크박스 체크된 경우 초대자 id, 초대코드도 공란 있는 지 검사
                            isValid = !(binding.hostIdEt.text.isNullOrBlank()
                                    ||binding.inviteCodeEt.text.isNullOrBlank()
                                    ||!isValid)
                        }

                        if(isValid){//회원가입 폼이 유효한지 검사
                            if(binding.isInvitedCb.isChecked){//초대받은 신규가입 구현
                                am.signInWithInvite(
                                    this
                                    ,binding.idEt.text.toString()
                                    ,binding.pwEt.text.toString()
                                    ,binding.groupCodeEt.text.toString()
                                    ,binding.hostIdEt.text.toString()
                                    ,binding.inviteCodeEt.text.toString()
                                    ,binding.nameEt.text.toString()
                                    ,binding.militaryIdEt.text.toString().toInt()
                                    ,binding.heightEt.text.toString().toInt()
                                    ,binding.weightEt.text.toString().toInt()
                                    ,if(binding.ageEt.text.toString().isBlank()) null else binding.ageEt.text.toString().toInt()
                                    ,binding.bloodTypeSp.selectedItemPosition
                                    ,if(binding.goalOfWeightEt.text.toString().isBlank()) null else binding.goalOfWeightEt.text.toString().toInt()
                                    ,binding.goalOfTotalRankSp.selectedItemPosition
                                    ,binding.goalOfLegTuckRankSp.selectedItemPosition
                                    ,binding.goalOfShuttleRunRankSp.selectedItemPosition
                                    ,binding.goalOfFieldTrainingRankSp.selectedItemPosition
                                )
                            }
                            else{//초대받지 않은 신규가입
                                am.signInWithoutInvite(
                                    this
                                    ,binding.idEt.text.toString()
                                    ,binding.pwEt.text.toString()
                                    ,binding.groupCodeEt.text.toString()
                                    ,binding.nameEt.text.toString()
                                    ,binding.militaryIdEt.text.toString().toInt()
                                    ,binding.heightEt.text.toString().toInt()
                                    ,binding.weightEt.text.toString().toInt()
                                    ,if(binding.ageEt.text.toString().isBlank()) null else binding.ageEt.text.toString().toInt()
                                    ,binding.bloodTypeSp.selectedItemPosition
                                    ,if(binding.goalOfWeightEt.text.toString().isBlank()) null else binding.goalOfWeightEt.text.toString().toInt()
                                    ,binding.goalOfTotalRankSp.selectedItemPosition
                                    ,binding.goalOfLegTuckRankSp.selectedItemPosition
                                    ,binding.goalOfShuttleRunRankSp.selectedItemPosition
                                    ,binding.goalOfFieldTrainingRankSp.selectedItemPosition
                                )
                            }
                        }else{
                            showDialogMessage("입력 오류", "필수 항목이 다 작성되어있는 지, 비밀번호 확인이 맞는 지 확인해 주세요")
                        }
                    }
                }
        }
        binding.cancelLl.setOnClickListener {
            finish()
        }

    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }
}
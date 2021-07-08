package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityInviteCodeIssueBinding

class InviteCodeIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteCodeIssueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteCodeIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        //발급절차가 끝난 상태인지 파악하고 UI를 발급가능 상태로 변화시키는 과정을 구현할 것
        //만약 아직 발급중이면 발급중인 UI상태로 유지
        val ud = UserData.getInstance()
        if(ud.inviteHashCode == null){ //코드가 아직 미발급인 상태
            binding.issuingTv.text = "코드발급"
            binding.issuingCv.setCardBackgroundColor(Color.WHITE)
            binding.inviteCodeEt.inputType = InputType.TYPE_CLASS_NUMBER
            binding.issuingLl.isClickable = true //연타방지 해제

            binding.issuingLl.setOnClickListener{
                val am = AccountManager()
                if(!am.checkNetworkState(this)){//네트워크 연결 체크
                    showDialogMessage("네트워크 오류", "네트워크 연결 상태를 확인해 주세요")
                    return@setOnClickListener
                }
                else if(binding.inviteCodeEt.text.isNullOrBlank()){
                    showDialogMessage("입력 오류", "초대코드를 입력해 주세요")
                    return@setOnClickListener
                }

                binding.issuingTv.text = "발급중"
                binding.issuingCv.setCardBackgroundColor(Color.GRAY)
                binding.inviteCodeEt.inputType = InputType.TYPE_NULL
                binding.issuingLl.isClickable = false//연타방지

                am.issueInviteCode(this, binding.inviteCodeEt.text.toString(), binding.isAdminCb.isChecked)
            }
        }
        else{//코드가 발급중인 상태
            binding.inviteCodeEt.setText(ud.inviteHashCode)
            binding.issuingTv.text = "발급중"
            binding.issuingCv.setCardBackgroundColor(Color.GRAY)
            binding.inviteCodeEt.inputType = InputType.TYPE_NULL
            binding.issuingLl.isClickable = false//연타방지
        }

        binding.cancelLl.setOnClickListener {
            finish()
        }
    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { _, _ -> }
        builder.show()
    }
}
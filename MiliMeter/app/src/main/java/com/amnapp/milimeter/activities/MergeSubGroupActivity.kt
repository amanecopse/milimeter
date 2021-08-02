package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.databinding.ActivityLoginBinding
import com.amnapp.milimeter.databinding.ActivityMergeSubGroupBinding

class MergeSubGroupActivity : AppCompatActivity() {
    lateinit var binding: ActivityMergeSubGroupBinding
    lateinit var mLoadingDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mDialog.show()로 시작

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMergeSubGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setProgressDialog()

        binding.mergeBt.setOnClickListener {
            binding.mergeBt.isClickable = false
            mLoadingDialog.show()
            AccountManager().mergeSubGroup(
                applicationContext,
                binding.idEt.text.toString(),
                binding.pwEt.text.toString(),
                binding.subGroupCodeEt.text.toString()
            ){resultMessage ->
                when(resultMessage){
                    AccountManager.ERROR_NOT_FOUND_ID ->{
                        showDialogMessage("오류", "아이디나 그룹이 존재하지 않습니다"){}
                    }
                    AccountManager.ERROR_WRONG_PASSWORD ->{
                        showDialogMessage("오류", "비밀번호가 옳지 않습니다"){}
                    }
                    AccountManager.ERROR_WRONG_INFO ->{
                        showDialogMessage("오류", "그룹코드가 옳지 않거나 초대하는 유저가 Master 권한을 갖고 있지 않습니다"){}
                    }
                    AccountManager.ERROR_NETWORK_NOT_CONNECTED ->{
                        showDialogMessage("오류", "네트워크 연결을 확인해주세요"){}
                    }
                    AccountManager.RESULT_SUCCESS ->{
                        showDialogMessage("그룹병합완료", "이 그룹을 하위그룹으로 병합하였습니다"){}
                    }
                    AccountManager.RESULT_FAILURE ->{
                        showDialogMessage("그룹병합실패", "병합할 수 없는 그룹입니다"){}
                    }
                }
                mLoadingDialog.dismiss()
                binding.mergeBt.isClickable = true
            }
        }
        binding.backIb.setOnClickListener {
            finish()
        }
        binding.cancelIb.setOnClickListener {
            finish()
        }
    }
    fun showDialogMessage(title: String, body: String, callBack: () -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack()}
        builder.show()
    }

    fun setProgressDialog() {
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = "Loading ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(ll)
        mLoadingDialog = builder.create()
    }
}
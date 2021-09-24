package com.amnapp.milimeter.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityUserPasswardBinding

class UserPasswardActivity :CustomThemeActivity() {

    lateinit var binding : ActivityUserPasswardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityUserPasswardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = UserData.getInstance()

        // 창닫기
        binding.cancelIb.setOnClickListener {
            finish()
        }
        binding.backIb.setOnClickListener {
            finish()
        }

        binding.changePwLl.setOnClickListener {

            // 현재 비밀번호 확인 다이얼로그 창
            val dialog1 = PwCheckDialog(this)
            dialog1.recordDialog()

            dialog1.RecordDialog()

            dialog1.setOnClickedListener(object : PwCheckDialog.ButtonClickListener {
                override fun onClicked(pw: String) {

                    // 새로운 비밀번호로 변경
                    if (user.pw == pw) {
                        val dialog2 = PwChangeDialog(this@UserPasswardActivity)
                        dialog2.recordDialog()

                        dialog2.RecordDialog()

                        dialog2.setOnClickedListener(object : PwChangeDialog.ButtonClickListener {
                            override fun onClicked(newPw: String) {
                                if (user.pw == newPw) {
                                   // 변경하기 전과 같으면 오류
                                    Toast.makeText(this@UserPasswardActivity, "잘못된 정보입니다", Toast.LENGTH_LONG).show()
                                } else if (newPw.isNullOrEmpty() ||newPw.length < 5 ||newPw.length > 20) {
                                    Toast.makeText(this@UserPasswardActivity, "잘못된 정보입니다", Toast.LENGTH_LONG).show()
                                } else {
                                    user.pw = newPw
                                    UserData.setInstance(user)
                                    // 서버에 저장
                                    AccountManager().uploadUserData(user){ message ->
                                        if(message == AccountManager.RESULT_SUCCESS)
                                            Toast.makeText(this@UserPasswardActivity, "비밀번호가 변경되었습니다", Toast.LENGTH_LONG).show()
                                    }

                                }
                            }
                        })
                    } else {
                      // 다른 비번이면 오류
                      Toast.makeText(this@UserPasswardActivity, "잘못된 정보입니다", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }

        binding.groupAccountLl.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}

// 비밀번호 확인 다이얼로그
class PwCheckDialog(context: Context) {
    private val dialog = Dialog(context)

    fun recordDialog() {
        dialog.show()
    }

    fun RecordDialog() {
        dialog.setContentView(R.layout.passwardchange_dialog1)

        // dialog 크기 설정
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        // 다이얼로그 버튼 클릭시 이벤트처리
        val edit = dialog.findViewById<EditText>(R.id.nowPasswardEt)
        val btDone = dialog.findViewById<Button>(R.id.checkBt)
        val btCancel = dialog.findViewById<Button>(R.id.cancelBt)

        btDone.setOnClickListener {
            onClickedListener.onClicked(edit.text.toString())
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    interface ButtonClickListener {
        fun onClicked(type: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

}

// 비밀번호 변경 다이얼로그
class PwChangeDialog(context: Context) {
    private val dialog = Dialog(context)

    fun recordDialog() {
        dialog.show()
    }

    fun RecordDialog() {
        dialog.setContentView(R.layout.passwardchange_dialog2)

        // dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        // 다이얼로그 버튼 클릭시 이벤트처리
        val edit = dialog.findViewById<EditText>(R.id.newPasswardEt)
        val btDone = dialog.findViewById<Button>(R.id.saveBt)
        val btCancel = dialog.findViewById<Button>(R.id.cancelBt)

        btDone.setOnClickListener {
            onClickedListener.onClicked(edit.text.toString())
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    interface ButtonClickListener {
        fun onClicked(type: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}
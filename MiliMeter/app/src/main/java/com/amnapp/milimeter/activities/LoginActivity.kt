package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityLoginBinding
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var mDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mDialog.show()로 시작

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setProgressDialog() // 로딩다이얼로그 세팅

        //초기상태 로그인이 되었는 지 아닌지에 따라 화면 결정
        val ud = UserData.getInstance()
        if(ud.isLogined){
            binding.profileCiv.visibility = View.VISIBLE
            binding.loginBoxCv.visibility = View.GONE
            binding.loginLl.visibility = View.GONE
            binding.signInCv.visibility = View.GONE
            binding.logoutLl.visibility = View.VISIBLE
            binding.issueCv.visibility = View.VISIBLE
            binding.adminCv.visibility = if(ud.isAdmin) View.VISIBLE else View.GONE
            binding.loginoutCv.setCardBackgroundColor(Color.RED)
        }
        else{
            binding.profileCiv.visibility = View.GONE
            binding.loginBoxCv.visibility = View.VISIBLE
            binding.loginLl.visibility = View.VISIBLE
            binding.signInCv.visibility = View.VISIBLE
            binding.logoutLl.visibility = View.GONE
            binding.issueCv.visibility = View.GONE
            binding.adminCv.visibility = View.GONE
            binding.loginoutCv.setCardBackgroundColor(Color.WHITE)

            val pm = PreferenceManager()
            val loginDataArray = pm.getLoginData(this)// 로그인 데이터를 로컬저장소에서 로드함
            binding.idEt.setText(loginDataArray[0])
            binding.pwEt.setText(loginDataArray[1])
            binding.groupCodeEt.setText(loginDataArray[2])
        }

        binding.loginLl.setOnClickListener{
            val id = binding.idEt.text.toString()
            val pw = binding.pwEt.text.toString()
            val groupCode = binding.groupCodeEt.text.toString()
            val pm = PreferenceManager()
            val am = AccountManager()
            am.login(
                this,
                id,
                pw,
                groupCode
            )
            pm.setLoginData(this, id, pw, groupCode)//로컬저장소에 로그인 데이터 저장
        }
        binding.logoutLl.setOnClickListener {
            val am = AccountManager()
            am.logout(this)
        }
        binding.issueLl.setOnClickListener {
            val intent = Intent(this, InviteCodeIssueActivity::class.java)
            startActivity(intent)
        }
        binding.signInLl.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.adminLl.setOnClickListener {
            val intent = Intent(this, AdminPageActivity::class.java)
            startActivity(intent)
        }
        binding.cancelLl.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TAG = "%%%%%LoginActivityLog%%%%%"
    }

    fun showDialogMessage(title: String, body: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> }
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
        mDialog = builder.create()
//        mDialog.show()
//        val window: Window? = mDialog.window
//        if (window != null) {
//            val layoutParams = WindowManager.LayoutParams()
//            layoutParams.copyFrom(mDialog.window!!.attributes)
//            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
//            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
//            mDialog.window!!.attributes = layoutParams
//        }
    }
}
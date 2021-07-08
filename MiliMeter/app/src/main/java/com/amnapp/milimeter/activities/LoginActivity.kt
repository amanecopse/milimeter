package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.activities.InviteCodeIssueActivity
import com.amnapp.milimeter.activities.SignInActivity
import com.amnapp.milimeter.databinding.ActivityLoginBinding
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        //초기상태 로그인, 회원가입
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

        }

        binding.loginLl.setOnClickListener{
            val am = AccountManager()
            am.login(this, binding.idEt.text.toString(), binding.pwEt.text.toString(), binding.groupCodeEt.text.toString())
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

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }
}
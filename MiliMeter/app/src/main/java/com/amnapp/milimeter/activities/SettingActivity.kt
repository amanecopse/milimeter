package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityInviteSubUserBinding
import com.amnapp.milimeter.databinding.ActivitySettingBinding
import kotlin.system.exitProcess

class SettingActivity : CustomThemeActivity() {
    lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 테마설정창 이동
        binding.themeSettingBt.setOnClickListener {
            val intentTheme = Intent(this, ThemeActivity::class.java)
            startActivity(intentTheme)
        }
        // 언어설정창 이동
        binding.languageSettingBt.setOnClickListener {
            val intentLanguage = Intent(this, LanguageActivity::class.java)
            startActivity(intentLanguage)
        }
        // 알림설정창 이동
        binding.noticeSettingBt.setOnClickListener {
            val intentNotice = Intent(this, NoticeActivity::class.java)
            startActivity(intentNotice)
        }
        // 회원정보창 이동(비밀번호변경)
        binding.privateSettingBt.setOnClickListener {
            val intentPrivate = Intent(this, UserPasswardActivity::class.java)
            startActivity(intentPrivate)
        }
        //개인정보관리창 이동(사용자정보)
        binding.userSettingBt.setOnClickListener {
            val intentUser = Intent(this, UserInformationActivity::class.java)
            startActivity(intentUser)
        }


       // home창으로 이동
        binding.homeBt.setOnClickListener {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
        }
        // body창으로 이동
        binding.bodyBt.setOnClickListener {
            val intentBody = Intent(this, BodyActivity::class.java)
            startActivity(intentBody)
        }
       // result화면으로 이동
        binding.resultBt.setOnClickListener {
            val intentResult = Intent(this, ResultActivity::class.java)
            startActivity(intentResult)
        }
       // goal창으로 이동
        binding.goalBt.setOnClickListener {
            val intentGoal = Intent(this, GoalActivity::class.java)
            startActivity(intentGoal)
        }
       // setting창으로 이동
        binding.settingBt.setOnClickListener {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
        }

    }

    fun showTwoButtonDialogMessage(title: String, body: String, callBack: (Int) -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.show()
    }

    override fun onBackPressed() {
        showTwoButtonDialogMessage("알림", "Mili Meter를 종료하시겠습니까?"){
            when(it){
                -1 -> {
                    finishAffinity()
                    exitProcess(0)
                }
            }
        }
    }
}


package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 테마설정창 이동
        findViewById<Button>(R.id.themeSettingBt).setOnClickListener {
            val intentTheme = Intent(this, ThemeActivity::class.java)
            startActivity(intentTheme)
            finish()
        }
        // 언어설정창 이동
        findViewById<Button>(R.id.languageSettingBt).setOnClickListener {
            val intentLanguage = Intent(this, LanguageActivity::class.java)
            startActivity(intentLanguage)
            finish()
        }
        // 알림설정창 이동
        findViewById<Button>(R.id.noticeSettingBt).setOnClickListener {
            val intentNotice = Intent(this, NoticeActivity::class.java)
            startActivity(intentNotice)
            finish()
        }
        // 회원정보창 이동(비밀번호변경)
        findViewById<Button>(R.id.privateSettingBt).setOnClickListener {
            val intentPrivate = Intent(this, UserPasswardActivity::class.java)
            startActivity(intentPrivate)
            finish()
        }
        //개인정보관리창 이동(사용자정보)
        findViewById<Button>(R.id.userSettingBt).setOnClickListener {
            val intentUser = Intent(this, UserInformationActivity::class.java)
            startActivity(intentUser)
            finish()
        }


       // home창으로 이동
        findViewById<Button>(R.id.homeBt).setOnClickListener {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
            finish()
        }
        // body창으로 이동
        findViewById<Button>(R.id.bodyBt).setOnClickListener {
            val intentBody = Intent(this, BodyActivity::class.java)
            startActivity(intentBody)
            finish()
        }
       // result화면으로 이동
        findViewById<Button>(R.id.resultBt).setOnClickListener {
            val intentResult = Intent(this, ResultActivity::class.java)
            startActivity(intentResult)
            finish()
        }
       // goal창으로 이동
        findViewById<Button>(R.id.goalBt).setOnClickListener {
            val intentGoal = Intent(this, GoalActivity::class.java)
            startActivity(intentGoal)
            finish()
        }
       // setting창으로 이동
        findViewById<Button>(R.id.settingBt).setOnClickListener {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
            finish()
        }

    }
}


package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 각 설정 창으로 이동 -> 테마설정창 이동
        findViewById<Button>(R.id.themeSettingBt).setOnClickListener {
            val intentTheme = Intent(this, ThemeActivity::class.java)
            startActivity(intentTheme)
        }
        // 각 설정 창으로 이동 -> 언어설정창 이동
        findViewById<Button>(R.id.languageSettingBt).setOnClickListener {
            val intentLanguage = Intent(this, LanguageActivity::class.java)
            startActivity(intentLanguage)
        }
        // 각 설정 창으로 이동 -> 알림설정창 이동
        findViewById<Button>(R.id.alarmSettingBt).setOnClickListener {
            val intentNotice = Intent(this, NoticeActivity::class.java)
            startActivity(intentNotice)
        }
        // 각 설정 창으로 이동 -> 개인정보설정창 이동
        findViewById<Button>(R.id.privateSettingBt).setOnClickListener {
            val intentPrivate = Intent(this, PrivateAdministrationActivity::class.java)
            startActivity(intentPrivate)
        }
        // 각 설정 창으로 이동 -> 회원정보설정창 이동
        findViewById<Button>(R.id.userSettingBt).setOnClickListener {
            val intentUser = Intent(this, UserInformationActivity::class.java)
            startActivity(intentUser)
        }

        // 각 아이콘 창으로 이동 -> home창
        findViewById<Button>(R.id.homeBt).setOnClickListener {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
        }
        // 담당 미정 - body창(합친 뒤에 주석 없애주세요)
//        findViewById<Button>(R.id.bodyBt).setOnClickListener {
//            val intentBody = Intent(this, BodyActivity::class.java)
//            startActivity(intentBody)
//        }

        // 각 아이콘 창으로 이동 -> result창
        findViewById<Button>(R.id.resultBt).setOnClickListener {
            val intentResult = Intent(this, ResultActivity::class.java)
            startActivity(intentResult)
        }
        // 각 아이콘 창으로 이동 -> goal창
        findViewById<Button>(R.id.goalBt).setOnClickListener {
            val intentGoal = Intent(this, GoalActivity::class.java)
            startActivity(intentGoal)
        }
        // 각 아이콘 창으로 이동 -> setting창
        findViewById<Button>(R.id.settingBt).setOnClickListener {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
        }
    }
}


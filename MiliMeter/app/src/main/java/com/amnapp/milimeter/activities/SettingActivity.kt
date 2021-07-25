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

        // 테마설정창 이동
        findViewById<Button>(R.id.themeSettingBt).setOnClickListener {
            val intentTheme = Intent(this, ThemeActivity::class.java)
            startActivity(intentTheme)
        }
        // 언어설정창 이동
        findViewById<Button>(R.id.languageSettingBt).setOnClickListener {
            val intentLanguage = Intent(this, LanguageActivity::class.java)
            startActivity(intentLanguage)
        }
        // 알림설정창 이동
        findViewById<Button>(R.id.noticeSettingBt).setOnClickListener {
            val intentNotice = Intent(this, NoticeActivity::class.java)
            startActivity(intentNotice)
        }
        // 개인설정창 이동
        findViewById<Button>(R.id.debugBt).setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.debugBt).setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }

        binding.privateSettingBt.setOnClickListener {
            val intent = Intent(this, PrivateAdministrationActivity::class.java)
            startActivity(intent)
        }
        binding.userSettingBt.setOnClickListener {
            val intent = Intent(this, UserInformationActivity::class.java)
            startActivity(intent)
        }

        // 각 아이콘 창으로 이동 -> 아이콘 버튼 클릭시 화면 전환
        binding.homeBt.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        // 담당 미정 - body창(합친 뒤에 주석 없애주세요)
//        binding.bodyBt.setOnClickListener {
//            val intent = Intent(this, BodyActivity::class.java)
//            startActivity(intent)
//        }
        // 고동현님 프로젝트 담당 부분 - result창(합친 뒤에 주석 없애주세요)
//        binding.resultBt.setOnClickListener {
//            val intent = Intent(this, ResultActivity::class.java)
//            startActivity(intent)
//        }
        // 이다현님 프로젝트 담당 부분 - goal창(합친 뒤에 주석 없애주세요)
//        binding.goalBt.setOnClickListener {
//            val intent = Intent(this, GoalActivity::class.java)
//            startActivity(intent)
//        }
        binding.settingBt.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}


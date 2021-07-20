package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 각 설정 창으로 이동 -> 버튼클릭시 화면 전환
        binding.themeSettingBt.setOnClickListener {
            val intent = Intent(this, ThemeActivity::class.java)
            startActivity(intent)
        }
        binding.languageSettingBt.setOnClickListener {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
        }
        binding.alarmSettingBt.setOnClickListener {
            val intent = Intent(this, AlarmActivity::class.java)
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
        binding.bodyBt.setOnClickListener {
            val intent = Intent(this, BodyActivity::class.java)
            startActivity(intent)
        }
        // 고동현님 프로젝트 담당 부분 - result창(합친 뒤에 주석 없애주세요)
        binding.resultBt.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
        // 이다현님 프로젝트 담당 부분 - goal창(합친 뒤에 주석 없애주세요)
        binding.goalBt.setOnClickListener {
            val intent = Intent(this, GoalActivity::class.java)
            startActivity(intent)
        }
        binding.settingBt.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}


package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityBasicThemeSettingBinding

class BasicThemeSettingActivity : AppCompatActivity() {

    val binding by lazy { ActivityBasicThemeSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        binding.cancelBt.setOnClickListener {
            val intentBack = Intent(this, ThemeActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        // 모드 종류 설정하기
        binding.modeSettingRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 기본 모드
                R.id.basicModeRBt -> {

                }
                // 다크 모드
                R.id.darkModeRBt -> {

                }
                // 색상 모드
                R.id.colorModeRBt -> {

                }
            }
        }

    }
}
package com.amnapp.milimeter.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivitySpecialThemeSettingBinding

class SpecialThemeSettingActivity : AppCompatActivity() {

    val binding by lazy { ActivitySpecialThemeSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_special_theme_setting)

        // 창닫기
        with(binding) {
            saveBt.setOnClickListener{
                finish()
            }
        }

    }
}
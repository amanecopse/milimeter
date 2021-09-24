package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityThemeBinding


class ThemeActivity : AppCompatActivity() {

    val binding by lazy { ActivityThemeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        binding.cancelIb.setOnClickListener {
            finish()
        }
        binding.backIb.setOnClickListener {
            finish()
        }

        // 기본 테마창으로 이동
        binding.basicThemeLl.setOnClickListener {
            val intentBasicTheme = Intent(this, BasicThemeSettingActivity::class.java)
            startActivity(intentBasicTheme)
        }
        // 특별한 테마창으로 이동
        binding.specialThemeLl.setOnClickListener {
            val intentSpecialTheme = Intent(this, SpecialThemeSettingActivity::class.java)
            startActivity(intentSpecialTheme)
        }
        // 테마 제작하기창으로 이동
        binding.makingThemeLl.setOnClickListener {
            val intentMakingTheme = Intent(this, MakingThemeSettingActivity::class.java)
            startActivity(intentMakingTheme)
        }
    }
}
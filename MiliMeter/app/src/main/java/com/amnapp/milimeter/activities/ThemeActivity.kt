package com.amnapp.milimeter.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityThemeBinding


class ThemeActivity : CustomThemeActivity() {

    lateinit var binding : ActivityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityThemeBinding.inflate(layoutInflater)
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

    }
}
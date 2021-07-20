package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.databinding.ActivityThemeBinding


class ThemeActivity : AppCompatActivity() {

    val binding by lazy { ActivityThemeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        with(binding) {
            cancelBt.setOnClickListener{
                finish()
            }
        }
        // 기본 테마창으로 이동
        binding.basicThemeBt.setOnClickListener {
            val intent = Intent(this, BasicThemeSettingActivity::class.java).apply {
                startActivity(intent)
            }
        }
        // 특별한 테마창으로 이동
        binding.specialThemeBt.setOnClickListener {
            val intent = Intent(this, BasicThemeSettingActivity::class.java).apply {
                startActivity(intent)
            }
        }
        // 테마 제작하기창으로 이동
        binding.makingThemeBt.setOnClickListener {
            val intent = Intent(this, BasicThemeSettingActivity::class.java).apply {
                startActivity(intent)
            }
        }
    }
}
package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityMakingThemeSettingBinding

class MakingThemeSettingActivity : AppCompatActivity() {

    val binding by lazy { ActivityMakingThemeSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        binding.cancelBt.setOnClickListener {
            val intentBack = Intent(this, ThemeActivity::class.java)
            startActivity(intentBack)
            finish()
        }


    }
}
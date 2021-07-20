package com.amnapp.milimeter.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityMakingThemeSettingBinding

class MakingThemeSettingActivity : AppCompatActivity() {

    val binding by lazy { ActivityMakingThemeSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_theme_setting)


        with(binding) {
            // 창닫기
            saveBt.setOnClickListener{
                finish()
            }
            // 사진 가져오기
            photoGetBt.setOnClickListener{

            }
        }
    }
}
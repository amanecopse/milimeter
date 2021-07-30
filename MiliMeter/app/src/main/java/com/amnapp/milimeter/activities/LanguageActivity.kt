package com.amnapp.milimeter.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityLanguageBinding
import java.util.*


class LanguageActivity : AppCompatActivity() {

    val binding by lazy { ActivityLanguageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        binding.cancelBt.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        // 아이콘 종류 설정하기
        // 방법 2가지 구상했는데 조금 더 다듬어야 할 것 같음(빌드,디버깅 결과 오류는 없습니다.)
        binding.languageRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 기본 아이콘
                R.id.basicIconRBt -> {

                }
                // 한국어 아이콘
                R.id.koreaIconRBt -> {

                }
                //영어 아이콘
                R.id.englishIconRBt -> {

                }
            }
        }
    }



}
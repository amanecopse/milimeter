package com.amnapp.milimeter.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity() {

    val binding by lazy { ActivityLanguageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        with(binding) {
            cancelBt.setOnClickListener{
                finish()
            }
        }
        // 아이콘 종류 설정하기
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
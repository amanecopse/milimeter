package com.amnapp.milimeter.activities

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
        with(binding) {
            cancelBt.setOnClickListener{
                finish()
            }
        }
        val homeIcon = resources.getString(R.string.home)
        val bodyIcon = resources.getString(R.string.body)
        val resultIcon = resources.getString(R.string.result)
        val goalIcon = resources.getString(R.string.goal)
        val settingIcon = resources.getString(R.string.setting)

        // 아이콘 종류 설정하기
        // 방법 2가지 구상했는데 조금 더 다듬어야 할 것 같음(빌드,디버깅 결과 오류는 없습니다.)
        binding.languageRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 기본 아이콘
                R.id.basicIconRBt -> {
                    Button(this).apply {
                        setText(R.string.home)
                    }
                    Button(this).apply {
                        setText(R.string.body)
                    }
                    Button(this).apply {
                        setText(R.string.result)
                    }
                    Button(this).apply {
                        setText(R.string.goal)
                    }
                    Button(this).apply {
                        setText(R.string.setting)
                    }
                }
                // 한국어 아이콘
                R.id.koreaIconRBt -> {
                    Button(this).apply {
                        setText(R.string.home)
                    }
                    Button(this).apply {
                        setText(R.string.body)
                    }
                    Button(this).apply {
                        setText(R.string.result)
                    }
                    Button(this).apply {
                        setText(R.string.goal)
                    }
                    Button(this).apply {
                        setText(R.string.setting)
                    }
                }
                //영어 아이콘
                R.id.englishIconRBt -> {
                    Button(this).apply {
                        setText(R.string.home)
                    }
                    Button(this).apply {
                        setText(R.string.body)
                    }
                    Button(this).apply {
                        setText(R.string.result)
                    }
                    Button(this).apply {
                        setText(R.string.goal)
                    }
                    Button(this).apply {
                        setText(R.string.setting)
                    }
                }
            }
        }
    }
    // locale 사용하는 방법 공부중 현재 진행중
    private fun changeLocale(localeLang:String) {
        var locale: Locale? = null

        when (localeLang) {
            "ko" -> locale =Locale("ko")
            "en" -> locale = Locale("en")
            "ru" -> locale = Locale("ru")
        }

        val config: Configuration = getResources().getConfiguration()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        var list = mutableListOf<String>()
        list.add(getString(R.string.home))
        list.add(getString(R.string.body))
        list.add(getString(R.string.result))
        list.add(getString(R.string.goal))
        list.add(getString(R.string.setting))
    }


}
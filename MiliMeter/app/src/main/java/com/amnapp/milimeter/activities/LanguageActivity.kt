package com.amnapp.milimeter.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.LocaleWrapper
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityLanguageBinding
import java.util.*



class LanguageActivity : AppCompatActivity() {

    val binding by lazy { ActivityLanguageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 0일때 한국어, 1일때 영어로 상태값 저장
        if (PreferenceManager().getLanguageData(this).toString() == "en") {
            binding.englishRBt.setChecked(true)
        } else if (PreferenceManager().getLanguageData(this).toString() == "ko"){
            binding.koreaRBt.setChecked(true)
        }


        // 창닫기
        binding.cancelBt.setOnClickListener {
            val intentcancel = Intent(this, SettingActivity::class.java)
            startActivity(intentcancel)
            finish()
        }

        binding.backBt.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        binding.languageRG.setOnCheckedChangeListener { group, checkedId ->
           when(checkedId) {
               R.id.koreaRBt -> {
                   PreferenceManager().setLanguageData(this,"ko").toString()
               }
               R.id.englishRBt -> {
                   PreferenceManager().setLanguageData(this,"en").toString()
               }
           }
        }


        binding.saveBt.setOnClickListener {
            changeLanguage(PreferenceManager().getLanguageData(this).toString())
        }
    }

    fun changeLanguage(lang: String?) {
        LocaleWrapper.setLocale(lang)
        recreate()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleWrapper.wrap(it) })
    }

}



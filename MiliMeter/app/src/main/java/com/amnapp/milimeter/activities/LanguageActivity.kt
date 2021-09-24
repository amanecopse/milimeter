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

        if (PreferenceManager().getLanguageData(this).toString() == "en") {
            binding.englishRBt.setChecked(true)
        } else if (PreferenceManager().getLanguageData(this).toString() == "ko"){
            binding.koreaRBt.setChecked(true)
        }


        // 창닫기
        binding.cancelIb.setOnClickListener {
            finish()
        }
        binding.backIb.setOnClickListener {
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


        binding.saveCv.setOnClickListener {
            changeLanguage(PreferenceManager().getLanguageData(this).toString())
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
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



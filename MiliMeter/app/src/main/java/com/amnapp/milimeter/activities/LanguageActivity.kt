package com.amnapp.milimeter.activities

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
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
            val intentcancel = Intent(this, SettingActivity::class.java)
            startActivity(intentcancel)
            finish()
        }

        binding.backBt.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
            finish()
        }


        binding.saveBt.setOnClickListener {
            recreate()
            finish()
        }



    }

    // locale을 통한 언어 설정 sLocale에 Locale형태로 언어 넣기(default일때 한국어로 설정)
    var sLocale: Locale? = Locale("ko")

    fun wrap(base: Context?) : Context {
        val res: Resources = base!!.getResources()
        val config = res.configuration
        config.setLocale(sLocale)
        return base.createConfigurationContext(config)
    }

    fun setLocale(lang: String?) {
        sLocale = Locale(lang)
    }

    fun changeLanguage(lang: String?) {
        setLocale(lang)
        recreate()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(wrap(newBase))
    }

}


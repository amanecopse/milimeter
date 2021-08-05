package com.amnapp.milimeter.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
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
        if (PreferenceManager().getOnOff(this).toString() == "en") {
            binding.englishRBt.setChecked(true)
        } else if (PreferenceManager().getOnOff(this).toString() == "ko"){
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
                   PreferenceManager().setOnOff(this,"ko").toString()
               }
               R.id.englishRBt -> {
                   PreferenceManager().setOnOff(this,"en").toString()
               }
           }
        }


        binding.saveBt.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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
        //recreate()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(wrap(newBase))
    }

}

open class BaseActivity : AppCompatActivity() {
    companion object {
        public var dLocale: Locale? = null
    }
    init {
        updateConfig(this)
    }
    fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale==Locale("") ) // Do nothing if dLocale is null
            return
        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
}



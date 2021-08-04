package com.amnapp.milimeter.activities

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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

        val itemList = listOf("선택하세요", "한국어", "영어")
        val adapter = ArrayAdapter(this, R.layout.activity_language, itemList)
        binding.languageSp.adapter = adapter
        binding.languageSp.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 1) {
                    setLocale("ko")
                } else if (position == 2) {
                    setLocale("en")
                } else {
                    binding.saveBt.setOnClickListener {
                        Toast.makeText(this@LanguageActivity, "두 언어중에서 선택해주세요", Toast.LENGTH_LONG).show()
                        finish()
                    }

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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


package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivitySpecialThemeSettingBinding


class SpecialThemeSettingActivity : CustomThemeActivity() {

    lateinit var binding: ActivitySpecialThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivitySpecialThemeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 창닫기 (바로 설정창으로)
        binding.cancelIb.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
            finish()
        }
        // 뒤로가기 (테마창으로)
        binding.backIb.setOnClickListener {
            finish()
        }

        // 테마색깔 설정하기
        if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_SPECIAL_FIRST) {
            binding.theme1RBt.setChecked(true)
        } else if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_SPECIAL_SECOND) {
            binding.theme2RBt.setChecked(true)
        } else if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_SPECIAL_THIRD) {
            binding.theme3RBt.setChecked(true)
        }

        binding.themeColorRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 테마1
                R.id.theme1RBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_SPECIAL_FIRST)
                    mode = "special"
                }
                // 테마2
                R.id.theme2RBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_SPECIAL_SECOND)
                    mode = "special"
                }
                // 테마3
                R.id.theme3RBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_SPECIAL_THIRD)
                    mode = "special"
                }
            }
        }

        binding.saveCv.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
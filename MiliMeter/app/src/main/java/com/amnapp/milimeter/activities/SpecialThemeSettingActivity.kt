package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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

        if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_LIGHTGREEN) {
            binding.lightGreenRBt.setChecked(true)
        } else if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_LIGHTLIME) {
            binding.greenRBt.setChecked(true)
        } else if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_GRAYGREEN) {
            binding.darkGreenRBt.setChecked(true)
        }

        binding.themeColorRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 기본 모드
                R.id.lightGreenRBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_LIGHTGREEN)
                    mode = "lightgreen theme"
                }
                // 다크 모드
                R.id.greenRBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_LIGHTLIME)
                    mode = "lightlime theme"
                }
                // 색상 모드
                R.id.darkGreenRBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_GRAYGREEN)
                    mode = "graygreen theme"
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
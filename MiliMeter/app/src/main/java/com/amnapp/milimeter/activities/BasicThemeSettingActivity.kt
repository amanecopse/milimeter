package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityBasicThemeSettingBinding
import java.time.temporal.TemporalAdjusters.next

class BasicThemeSettingActivity : CustomThemeActivity() {
    lateinit var binding: ActivityBasicThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()
        binding = ActivityBasicThemeSettingBinding.inflate(layoutInflater)
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

        // 모드 종류 설정하기

        if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_DEFAULT) {
            binding.basicModeRBt.setChecked(true)
        } else if (PreferenceManager().getThemeData(this) == PreferenceManager.THEME_DARK){
            binding.darkModeRBt.setChecked(true)
        } else {
            if (PreferenceManager().getThemeData(this) != PreferenceManager.THEME_SPECIAL_FIRST || PreferenceManager().getThemeData(this) != PreferenceManager.THEME_SPECIAL_SECOND || PreferenceManager().getThemeData(this) != PreferenceManager.THEME_SPECIAL_THIRD) {
                binding.colorModeRBt.setChecked(true)
                allColorBtClickChangeText()
                mode = "color"
            }
        }

        binding.modeSettingRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 기본 모드
                R.id.basicModeRBt -> {
                    reset()
                    colorBtInactivation()
                    pm.setThemeData(this, PreferenceManager.THEME_DEFAULT)
                    mode = "basic"
                }
                // 다크 모드
                R.id.darkModeRBt -> {
                    reset()
                    colorBtInactivation()
                    pm.setThemeData(this, PreferenceManager.THEME_DARK)
                    mode = "dark"
                }
                // 색상 모드
                R.id.colorModeRBt -> {
                    colorBtActivation()
                    showDialogMessage("알림", "색상표에서 원하시는 색을 선택해주세요.\n선택하지 않을경우 기본으로 자동설정됩니다.")
                    allColorBtClickChangeText()
                    mode = "color"
                }
            }
        }


        binding.saveCv.setOnClickListener {
            if (mode == "color") {
                findColor()
            }
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

}
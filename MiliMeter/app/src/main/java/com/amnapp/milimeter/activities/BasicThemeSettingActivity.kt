package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityBasicThemeSettingBinding

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
            binding.colorModeRBt.setChecked(true)
        }

        val pm = PreferenceManager()
        binding.modeSettingRG.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                // 기본 모드
                R.id.basicModeRBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_DEFAULT)
                }
                // 다크 모드
                R.id.darkModeRBt -> {
                    pm.setThemeData(this, PreferenceManager.THEME_DARK)
                }
                // 색상 모드
                R.id.colorModeRBt -> {
                    showDialogMessage("알림", "아래 색상표에서 원하시는 색상을 선택해주세요.")
                }
            }
        }

        binding.saveCv.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
    }

}
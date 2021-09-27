package com.amnapp.milimeter.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityMakingThemeSettingBinding

class MakingThemeSettingActivity : CustomThemeActivity() {

    lateinit var binding : ActivityMakingThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityMakingThemeSettingBinding.inflate(layoutInflater)
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

       binding.saveCv.setOnClickListener {
           finish()
       }


    }
}

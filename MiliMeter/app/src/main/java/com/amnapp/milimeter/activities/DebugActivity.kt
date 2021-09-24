package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityDebugBinding
import com.amnapp.milimeter.databinding.ActivitySignInBinding

class DebugActivity : CustomThemeActivity() {

    lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginActivityBt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.trainingRecordActivityBt.setOnClickListener {
            val intent = Intent(this, TrainingRecordActivity::class.java)
            startActivity(intent)
        }
        binding.themeExampleActivity.setOnClickListener {
            val intent = Intent(this, ThemeExampleActivity::class.java)
            startActivity(intent)
        }
    }
}
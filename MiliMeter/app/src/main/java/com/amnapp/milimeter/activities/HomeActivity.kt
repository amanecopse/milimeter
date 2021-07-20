package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBt.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.recordBt.setOnClickListener{
            val intent = Intent(this, TrainingRecordActivity::class.java)
            startActivity(intent)
        }
        binding.themeExampleBt.setOnClickListener{
            val intent = Intent(this, ThemeExampleActivity::class.java)
            startActivity(intent)
        }
    }
}
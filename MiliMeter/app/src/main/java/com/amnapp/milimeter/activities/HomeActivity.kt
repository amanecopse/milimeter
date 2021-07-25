package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        findViewById<Button>(R.id.debugBt).setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.resultBt).setOnClickListener {
            val bodyintent =Intent(this,ResultActivity::class.java)
            startActivity(bodyintent)
        }

        findViewById<Button>(R.id.settingBt).setOnClickListener {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
        }

    }
}
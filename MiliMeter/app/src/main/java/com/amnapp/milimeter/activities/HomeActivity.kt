package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.amnapp.milimeter.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//임주현 코드코드

        findViewById<Button>(R.id.debugBt).setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.resultBt).setOnClickListener {
            val bodyintent =Intent(this,ResultActivity::class.java)
            startActivity(bodyintent)
        }

        // 설정창 화면전환
        findViewById<Button>(R.id.settingBt).setOnClickListener {//디버그 창으로 가는 코드
            val settingintent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        // 충돌 확인용
    }
}
package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.databinding.ActivityTimeSettingBinding
import java.util.*

class TimeSettingActivity : AppCompatActivity() {

    val binding by lazy { ActivityTimeSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        binding.cancelBt.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
        }

        // 취소하기 버튼
        binding.backBt.setOnClickListener {
            val intentBack = Intent(this, NoticeActivity::class.java)
            startActivity(intentBack)
        }





    }
}
package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        val binding by lazy { ActivityNoticeBinding.inflate(layoutInflater) }

        // 창닫기
        with(binding) {
            cancelBt.setOnClickListener {
                finish()
            }

        }
        binding.noticeCheckSt.setOnCheckedChangeListener {buttonView, isChecked ->
            if(isChecked) {
                val intentAlarm = Intent(this, TimeSettingActivity::class.java)
                startActivity(intentAlarm)
            }
        }

    }
}
package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    val binding by lazy { ActivityNoticeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창닫기
        with(binding) {
            cancelBt.setOnClickListener {
                finish()
            }
        }

        binding.noticeCheckSt.setOnCheckedChangeListener {_, onSwitch ->
            if(onSwitch) {
                val intentNoticeSet = Intent(this, TimeSettingActivity::class.java)
                startActivity(intentNoticeSet)
            }
        }

    }
}
package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.amnapp.milimeter.R
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
            finish()
        }

        // 취소하기 버튼
        binding.backBt.setOnClickListener {
            val intentBack = Intent(this, NoticeActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        binding.firstNtSt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                var calendar = Calendar.getInstance()
                var hour = calendar.get(Calendar.HOUR_OF_DAY)
                var minute = calendar.get(Calendar.MINUTE)
                var am_pm = calendar.get(Calendar.AM_PM)
                var day = " "
                // am pm 오류 (계속 pm만 나옴)
                if (am_pm == Calendar.AM) {
                    day = "AM"
                } else if (am_pm == Calendar.PM) {
                    day = "PM"
                }
                // 24시간 표현을 12시간 표현으로 바꿀 것
                // 분이 일의 자리 수면 앞에 0 붙이기
                var listener = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    if (i < 10) {
                        binding.firstNtSt.text = "${day} 0${i%12} : ${i2}"
                    }
                    else {
                        binding.firstNtSt.text = "${day} ${i} : ${i2}"
                    }
                }

                var picker = TimePickerDialog(this, listener, hour, minute, false ) // true하면 24시간 제
                picker.show()
            }
        }

    }
}

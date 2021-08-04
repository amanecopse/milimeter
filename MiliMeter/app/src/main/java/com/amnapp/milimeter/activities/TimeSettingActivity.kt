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

        // 첫번째 알림 시간 설정
        binding.firstNtSt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                var calendar1 = Calendar.getInstance()
                var hour1 = calendar1.get(Calendar.HOUR_OF_DAY)
                var minute1 = calendar1.get(Calendar.MINUTE)
                var day1 = " "

                var listener1 = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->

                    // i => 0 ~ 23
                    if (i < 12) {
                        day1 = "AM"
                    } else {
                        day1 = "PM"
                    }

                    if (i2 < 10) {
                        if (i%12 == 0) {
                            binding.firstNtSt.text = "${day1} 12 : 0${i2}"
                        } else {
                            binding.firstNtSt.text = "${day1} ${i%12} : 0${i2}"
                        }

                    }
                    else {
                        if (i%12 == 0) {
                            binding.firstNtSt.text = "${day1} 12 : ${i2}"
                        } else {
                            binding.firstNtSt.text = "${day1} ${i%12} : ${i2}"
                        }
                    }
                }

                var picker = TimePickerDialog(this, listener1, hour1, minute1, false ) // true하면 24시간 제
                picker.show()
            }
        }

        // 두번째 알림 시간 설정
        binding.secondNtSt.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                var calendar2 = Calendar.getInstance()
                var hour2 = calendar2.get(Calendar.HOUR_OF_DAY)
                var minute2 = calendar2.get(Calendar.MINUTE)
                var day2 = " "

                var listener2 = TimePickerDialog.OnTimeSetListener { timePicker, j, j2 ->
                    // j => 0 ~ 23
                    if (j < 12) {
                        day2 = "AM"
                    } else {
                        day2 = "PM"
                    }

                    if (j2 < 10) {
                        if (j%12 == 0) {
                            binding.secondNtSt.text = "${day2} 12 : 0${j2}"
                        } else {
                            binding.secondNtSt.text = "${day2} ${j%12} : 0${j2}"
                        }

                    }
                    else {
                        if (j%12 == 0) {
                            binding.firstNtSt.text = "${day2} 12 : ${j2}"
                        } else {
                            binding.firstNtSt.text = "${day2} ${j%12} : ${j2}"
                        }
                    }
                }

                var picker = TimePickerDialog(this, listener2, hour2, minute2, false ) // true하면 24시간 제
                picker.show()
            }
        }



    }
}

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
        with(binding) {
            cancelBt.setOnClickListener {
                finish()
            }
        }
        // 취소하기 버튼
        binding.backBt.setOnClickListener {
            val intentBack = Intent(this, NoticeActivity::class.java)
            startActivity(intentBack)
        }

        // 오늘 날짜 구하기
        val cal = Calendar.getInstance()
        val yearT = cal.get(Calendar.YEAR).toString()
        val monthT = (cal.get(Calendar.MONTH)+1).toString()
        val dayT = cal.get(Calendar.DATE).toString()

        binding.dayTv.setText("오늘 - ${yearT}년 ${monthT}월 ${dayT}일")

        // 달력 버튼 클릭시 날짜 선택
        binding.calendarBt.setOnClickListener{
            var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)

            var listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                // i년 i2월 i3일
                binding.dayTv.text = "설정날짜 - ${i}년 ${i2 + 1}월 ${i3}일"
            }

            var picker = DatePickerDialog(this, listener, year, month, day)
            picker.show()
        }



    }
}
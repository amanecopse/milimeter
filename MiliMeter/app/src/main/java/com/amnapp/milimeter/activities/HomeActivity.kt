package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityHomeBinding
import com.amnapp.milimeter.databinding.ActivitySettingBinding
import java.util.*

class HomeActivity : AppCompatActivity() {

    val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        findViewById<Button>(R.id.debugBt).setOnClickListener {//디버그 창으로 가는 코드
            val intent = Intent(this, DebugActivity::class.java)
            startActivity(intent)
        }
        //Dday 날짜설정
//        binding.DdayBt.setOnClickListener {
//            val today = GregorianCalendar()
//           val year: Int = today.get(Calendar.YEAR)
//          val month: Int = today.get(Calendar.MONTH)
//          val date: Int = today.get(Calendar.DATE)
//
//          val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
//              override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//
//               }
//         }, year, month, date)
//        dlg.show()
//      }
    }
}
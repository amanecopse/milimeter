package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.databinding.ActivityNoticeBinding

class NoticeActivity : CustomThemeActivity() {

    lateinit var binding: ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 창닫기
        binding.cancelIb.setOnClickListener {
            finish()
        }
        binding.backIb.setOnClickListener {
            finish()
        }

        binding.noticeSetLl.setOnClickListener {
            showDialogMessageAndTimeSet("알림", "알림은 1개 설정할 수 있으며,\n알림시간은 2회 설정할 수 있습니다.")
        }

    }

    fun showDialogMessageAndTimeSet(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent(this, TimeSettingActivity::class.java)
            startActivity(intent)
        }
        builder.show()
    }

}
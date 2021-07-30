package com.amnapp.milimeter.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData.Companion.getInstance
import com.amnapp.milimeter.databinding.ActivityBodyBinding

class BodyActivity : AppCompatActivity() {

    val binding by lazy { ActivityBodyBinding.inflate(layoutInflater) }

    //핸들러사용
    val handler = Handler()
    var timeValue = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //핸들러 - 1초마다 실행되게 함
        val runnable = object : Runnable {
            override fun run() {
                timeValue++
                //TextView 업데이트 하기
                timeToText(timeValue)?.let {
                    binding.stopwatchTv.text = it
                }
                handler.postDelayed(this, 1000)
            }
        }

        //타이머 버튼 이벤트 처리 - 시작, 일시정지, 리셋, 기록
        var check = false // 같은버튼을 또 클릭할 때 중복되는 활동을 하지 않기 위함
        binding.startBt.setOnClickListener {
           if (check == false) {
               check = true
               handler.post(runnable)
           }
        }

        binding.stopBt.setOnClickListener {
            if (check == true) {
                check = false
            }
            handler.removeCallbacks(runnable)
        }

        binding.resetBt.setOnClickListener {
            if (check == true) {
                check = false
            }
            handler.removeCallbacks(runnable)
            timeValue = 0
            timeToText()?.let {
                binding.stopwatchTv.text = it
            }
        }

        binding.recordBt.setOnClickListener{
            recordTime()
            // 기록 다이얼로그 창
            val dialog = CustomDialog(this)
            dialog.recordDialog()

            dialog.RecordDialog()

            dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
                override fun onClicked(type: String) {
                    binding.checkTv.append(type)
                }
            })
        }

        // 텍스트뷰변환 - 목표몸무게
        var userWeight = getInstance().userWeight
        var goalWeight = getInstance().goalOfWeight
        if (goalWeight != null && userWeight != null) {
            if (goalWeight < userWeight) {
                binding.weightTv.text = "목표치보다 +${userWeight-goalWeight}kg"
            } else if (goalWeight > userWeight) {
                binding.weightTv.text = "목표치보다 -${goalWeight-userWeight}kg"
            } else if (goalWeight == userWeight) {
                binding.weightTv.text = "목표몸무게 도달!!"
            }
        }

        // 아이콘 버튼 화면전환 처리

        // home창으로 이동
        binding.homeBt.setOnClickListener {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
        }
        // body창으로 이동
        binding.bodyBt.setOnClickListener {
            val intentBody = Intent(this, BodyActivity::class.java)
            startActivity(intentBody)
        }
        // result화면으로 이동
        binding.resultBt.setOnClickListener {
            val intentResult = Intent(this, ResultActivity::class.java)
            startActivity(intentResult)
        }
        // goal창으로 이동
        binding.goalBt.setOnClickListener {
            val intentGoal = Intent(this, GoalActivity::class.java)
            startActivity(intentGoal)
        }
        // setting창으로 이동
        binding.settingBt.setOnClickListener {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
        }



    }
    //타이머시간 변환작업
    private fun timeToText(time: Int = 0): String? {
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
    // 타이머기록저장
    private fun recordTime() {
        val lapTime = timeValue // 함수 호출 시 시간(time) 저장

        // apply() 스코프 함수로, TextView를 생성과 동시에 초기화
        val textView = binding.checkTv.apply {
            setTextSize(20f) // fontSize 20 설정
        }
        textView.text = "${lapTime / 3600}:${lapTime % 3600 / 60}:${lapTime % 60}" // 출력할 시간 설정
    }



}
// 기록 다이얼로그
class CustomDialog(context: Context) {
    private val dialog = Dialog(context)

    fun recordDialog() {
        dialog.show()
    }

    fun RecordDialog() {
        dialog.setContentView(R.layout.record_dialog)

        // dialog 크기 설정
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        // 다이얼로그 버튼 클릭시 이벤트처리
        val edit = dialog.findViewById<EditText>(R.id.typeEt)
        val btDone = dialog.findViewById<Button>(R.id.saveBt)
        val btCancel = dialog.findViewById<Button>(R.id.cancelBt)

        btDone.setOnClickListener {
            onClickedListener.onClicked(edit.text.toString())
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    interface ButtonClickListener {
        fun onClicked(type: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

}
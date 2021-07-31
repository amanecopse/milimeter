package com.amnapp.milimeter.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.UserData.Companion.getInstance
import com.amnapp.milimeter.databinding.ActivityBodyBinding
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*
import kotlin.collections.ArrayList

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
                    val myUd = UserData.getInstance()
                    // 오늘 날짜 -> string형태
                    val nowDate = timeGenerator()
                    if(!myUd.login){
                        showDialogMessage("오류", "로그인 한 뒤 기록해 주세요")
                    }
                    else if(findViewById<EditText>(R.id.typeEt).text.isNullOrBlank()){
                        showDialogMessage("오류", "공란을 입력해 주세요")
                    }
                    // 데이터 해쉬맵으로 저장 -> timeValue는 초형태로 string형태
                    val record = hashMapOf(
                        type to "${timeValue%60}",
                        "date" to nowDate
                    )
                    val cm = ChartManager()
                    cm.updateTrainingRecord(UserData.getInstance(),nowDate,record,object: ChartManager.UICallBack{
                        override fun whatToDo() {
                            showDialogMessage("완료", "운동 결과를 기록했습니다")
                            // showChart()
                        }

                        override fun whatToDoWithLineDataSets(lineDataSets: MutableList<LineDataSet>, dateList: ArrayList<String>) {
                            return
                        }

                        override fun whatToDoWithDocuments(docs: MutableList<DocumentSnapshot>) {
                            return
                        }

                    })
                    binding.checkTv.append(type)
                }
            })
        }

        // 텍스트뷰변환 - 목표몸무게
        var userWeight = getInstance().weight
        var goalWeight = getInstance().goalOfWeight
        if (goalWeight != null && userWeight != null) {
            if (goalWeight < userWeight) {
                binding.weightTv.text = "목표치보다 +${userWeight.toInt()-goalWeight.toInt()}kg  :  과체중"
            } else if (goalWeight > userWeight) {
                binding.weightTv.text = "목표치보다 -${goalWeight.toInt()-userWeight.toInt()}kg  :  저체중"
            } else if (goalWeight == userWeight) {
                binding.weightTv.text = "목표몸무게 도달!!"
            }
        }

        // 아이콘 버튼 화면전환 처리

        // home창으로 이동
        binding.homeBt.setOnClickListener {
            val intentHome = Intent(this, HomeActivity::class.java)
            startActivity(intentHome)
            finish()
        }
        // body창으로 이동
        binding.bodyBt.setOnClickListener {
            val intentBody = Intent(this, BodyActivity::class.java)
            startActivity(intentBody)
            finish()
        }
        // result화면으로 이동
        binding.resultBt.setOnClickListener {
            val intentResult = Intent(this, ResultActivity::class.java)
            startActivity(intentResult)
            finish()
        }
        // goal창으로 이동
        binding.goalBt.setOnClickListener {
            val intentGoal = Intent(this, GoalActivity::class.java)
            startActivity(intentGoal)
            finish()
        }
        // setting창으로 이동
        binding.settingBt.setOnClickListener {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
            finish()
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

    fun showDialogMessage(title: String, body: String) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> finish() }
        builder.show()
    }

    fun timeGenerator() :String{
        val instance = Calendar.getInstance()
        val year = instance.get(Calendar.YEAR).toString()
        var month = (instance.get(Calendar.MONTH) + 1).toString()
        var date = instance.get(Calendar.DATE).toString()
        if (month.toInt() < 10) {
            month = "0${month}"
        }
        if (date.toInt() < 10) {
            date = "0${date}"
        }
        var now = "${year}.${month}.${date}"
        return now
    }

    /*
    private fun showChart() {
        val cm = ChartManager()
        // 만드신 차트 로드해주세요
        }
    }

     */

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
package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.DataUtil
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityResultBinding
import com.amnapp.milimeter.databinding.ActivityUserInformationBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.exitProcess


class ResultActivity : CustomThemeActivity() {

    lateinit var binding : ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //그래프


        //테이블
        getTabelData()
        //레그턱 가는 버튼
        var legButton = findViewById<Button>(R.id.legtuckbutton)
        legButton.setOnClickListener {
            val legintent = Intent(this, LegtuckResultActivity::class.java)
            startActivity(legintent)
        }
        //240m가는 버튼
        var runButton = findViewById<Button>(R.id.run)
        runButton.setOnClickListener {
            val runintent = Intent(this, RunningResultActivity::class.java)
            startActivity(runintent)
        }
        //전장순환
        var circuitButton = findViewById<Button>(R.id.circuit)
        circuitButton.setOnClickListener {
            val circuitintent = Intent(this, CircuitResultActivity::class.java)
            startActivity(circuitintent)
        }

        //그래프 적용
        findViewById<LineChart>(R.id.lineChart).apply {
            result_graph()
        }

        //홈버튼
        var homebtn = findViewById<Button>(R.id.homeBt)
        homebtn.setOnClickListener {
            val homeintent = Intent(this, HomeActivity::class.java)
            startActivity(homeintent)
        }

        //바디버튼
        var bodybtn = findViewById<Button>(R.id.bodyBt)
        bodybtn.setOnClickListener {
            val bodyintent = Intent(this, BodyActivity::class.java)
            startActivity(bodyintent)
        }

        //결과버튼
        var resultbtn = findViewById<Button>(R.id.resultBt)
        resultbtn.setOnClickListener {
            val resultintent = Intent(this, ResultActivity::class.java)
            startActivity(resultintent)
        }

        //목표버튼
        var goalBtn = findViewById<Button>(R.id.goalBt)
        goalBtn.setOnClickListener {
            val goalintent = Intent(this, GoalActivity::class.java)
            startActivity(goalintent)
        }

        //세팅버튼
        var optionBtn = findViewById<Button>(R.id.settingBt)
        optionBtn.setOnClickListener {
            val optionintent = Intent(this, SettingActivity::class.java)
            startActivity(optionintent)
        }

        binding.recordBt.setOnClickListener {
            showRecordDialog()
        }

    }

    private fun showRecordDialog() {//기록 다이얼로그 띄움
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_basic_record_input)

        val courseSp = dialog.findViewById<Spinner>(R.id.courseSp)
        val dateLl = dialog.findViewById<LinearLayout>(R.id.dateLl)
        val dateTv = dialog.findViewById<TextView>(R.id.dateTv)
        val recordEt = dialog.findViewById<EditText>(R.id.recordEt)
        val saveCv = dialog.findViewById<CardView>(R.id.saveCv)
        val cancelIb = dialog.findViewById<ImageButton>(R.id.cancelIb)

        val defaultDateMessage = dateTv.text

        dateLl.setOnClickListener {
            showDatePickerDialog(dateTv)
        }

        saveCv.setOnClickListener {
            val myUd = UserData.getInstance()
            val currDate = dateTv.text.toString()
            if(!myUd.login){
                showDialogMessage("오류", "로그인 한 뒤 기록해 주세요")
                return@setOnClickListener
            }
            else if(currDate == defaultDateMessage || recordEt.text.isNullOrEmpty()){
                showDialogMessage("오류", "입력하지 않은 정보가 있습니다")
                return@setOnClickListener
            }

            val course = arrayOf("legTuck", "shuttleRun", "fieldTraining", "weight")
            val record = hashMapOf(
                course[courseSp.selectedItemPosition] to recordEt.text.toString(),
                "date" to currDate
            )

            val cm = ChartManager()
            cm.updateTrainingRecord(UserData.getInstance(),currDate,record){
                showDialogMessage("완료", "운동 결과를 기록했습니다"){
                    dialog.dismiss()
                    recreate()
                }
            }
        }

        cancelIb.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDatePickerDialog(dateTv: TextView) {// 날짜 선택 다이얼로그 띄움
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateTv.text = ""+year+"."+ String.format("%02d", month)+"."+String.format("%02d", dayOfMonth)
        }
        val year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val month = SimpleDateFormat("MM").format(Date()).toInt()
        val day = SimpleDateFormat("dd").format(Date()).toInt()
        DatePickerDialog(this, callBack,year,month,day).show()
    }

    //그래프 생성
    private fun result_graph() {
        var linechart = findViewById<LineChart>(R.id.lineChart)
        val cm = ChartManager()
        cm.loadTrainingRecordNDaysAgo(UserData.getInstance(),cm.getCurrentDateBasedOnFormat(),7)
        { docs, lineDataSets, dateList ->

            //데이터 있으면
            if(dateList.size>1) {
                cm.makeLineChart(linechart, lineDataSets, dateList)
            }
            //데이터 아무것도 없을때
            else{

            }
        }
    }



    private fun getTabelData() {
        var cm = ChartManager()
        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            7
        ) { docs, lineDataSets, dateList ->

            val datelist = ArrayList<String>()
            val legTuckList = mutableListOf<Int>()
            val shuttleRunList = mutableListOf<Int>()
            val fieldTrainingList = mutableListOf<Int>()
            for (doc in docs) {
                doc.data?.get(ChartManager.LEG_TUCK)?.let { score ->
                    legTuckList.add(score.toString().toInt())

                }
                doc.data?.get(ChartManager.DATE)?.let { score ->
                    datelist.add(score.toString())

                }
                doc.data?.get(ChartManager.LEG_TUCK)?.let { score ->
                    legTuckList.add(score.toString().toInt())

                }
                doc.data?.get(ChartManager.SHUTTLE_RUN)?.let { score ->
                    shuttleRunList.add(score.toString().toInt())
                }
                doc.data?.get(ChartManager.FIELD_TRAINING)?.let { score ->
                    fieldTrainingList.add(score.toString().toInt())
                }
            }
            //다 차있을때
            if (datelist.size >= 3) {
                var date1 = findViewById<TextView>(R.id.date1)
                date1.setText(datelist.get(datelist.size - 3))
                var date2 = findViewById<TextView>(R.id.date2)
                date2.setText(datelist.get(datelist.size - 2))
                var date3 = findViewById<TextView>(R.id.date3)
                date3.setText(datelist.get(datelist.size - 1))

            }

            if (legTuckList.size >= 3) {
                //레그턱
                var leg1 = findViewById<TextView>(R.id.legrecord1)
                leg1.setText(legTuckList.get(legTuckList.size - 3).toString())
                var leg2 = findViewById<TextView>(R.id.legrecord2)
                leg2.setText(legTuckList.get(legTuckList.size - 2).toString())
                var leg3 = findViewById<TextView>(R.id.legrecord3)
                leg3.setText(legTuckList.get(legTuckList.size - 1).toString())
            }

            if (shuttleRunList.size >= 3) {
                //달리기
                var run1 = findViewById<TextView>(R.id.runrecord1)
                run1.setText(
                    String.format(
                        "%d분 %d초",
                        shuttleRunList.get(shuttleRunList.size - 3) / 60,
                        shuttleRunList.get(shuttleRunList.size - 3) % 60
                    )
                )
                var run2 = findViewById<TextView>(R.id.runrecord2)
                run2.setText(
                    String.format(
                        "%d분 %d초",
                        shuttleRunList.get(shuttleRunList.size - 2) / 60,
                        shuttleRunList.get(shuttleRunList.size - 2) % 60
                    )
                )
                var run3 = findViewById<TextView>(R.id.runrecord3)
                run3.setText(
                    String.format(
                        "%d분 %d초",
                        shuttleRunList.get(shuttleRunList.size - 1) / 60,
                        shuttleRunList.get(shuttleRunList.size - 1) % 60
                    )
                )
            }
            //전장순환

            if (fieldTrainingList.size >= 3) {
                var circuit1 = findViewById<TextView>(R.id.circuitrecord1)
                circuit1.setText(
                    String.format(
                        "%d분 %d초",
                        fieldTrainingList.get(fieldTrainingList.size - 3) / 60,
                        fieldTrainingList.get(fieldTrainingList.size - 3) % 60
                    )
                )
                var circuit2 = findViewById<TextView>(R.id.circuitrecord2)
                circuit2.setText(
                    String.format(
                        "%d분 %d초",
                        fieldTrainingList.get(fieldTrainingList.size - 2) / 60,
                        fieldTrainingList.get(fieldTrainingList.size - 2) % 60
                    )
                )
                var circuit3 = findViewById<TextView>(R.id.circuitrecord3)
                circuit3.setText(
                    String.format(
                        "%d분 %d초",
                        fieldTrainingList.get(fieldTrainingList.size - 1) / 60,
                        fieldTrainingList.get(fieldTrainingList.size - 1) % 60
                    )
                )
            }

            //2개만
            if (datelist.size == 2) {
                var date1 = findViewById<TextView>(R.id.date1)
                date1.setText(datelist.get(datelist.size - 2))
                var date2 = findViewById<TextView>(R.id.date2)
                date2.setText(datelist.get(datelist.size - 1))
            }

            if (legTuckList.size == 2) {
                //레그턱
                var leg1 = findViewById<TextView>(R.id.legrecord1)
                leg1.setText(legTuckList.get(legTuckList.size - 2).toString())
                var leg2 = findViewById<TextView>(R.id.legrecord2)
                leg2.setText(legTuckList.get(legTuckList.size - 1).toString())
            }

            if (shuttleRunList.size == 2) {
                //달리기
                var run1 = findViewById<TextView>(R.id.runrecord1)
                run1.setText(
                    String.format(
                        "%d분 %d초",
                        shuttleRunList.get(shuttleRunList.size - 2) / 60,
                        shuttleRunList.get(shuttleRunList.size - 2) % 60
                    )
                )
                var run2 = findViewById<TextView>(R.id.runrecord2)
                run2.setText(
                    String.format(
                        "%d분 %d초",
                        shuttleRunList.get(shuttleRunList.size - 1) / 60,
                        shuttleRunList.get(shuttleRunList.size - 1) % 60
                    )
                )

            }
            //전장순환
            if (fieldTrainingList.size == 2) {
                var circuit1 = findViewById<TextView>(R.id.circuitrecord1)
                circuit1.setText(
                    String.format(
                        "%d분 %d초",
                        fieldTrainingList.get(fieldTrainingList.size - 2) / 60,
                        fieldTrainingList.get(fieldTrainingList.size - 2) % 60
                    )
                )
                var circuit2 = findViewById<TextView>(R.id.circuitrecord2)
                circuit2.setText(
                    String.format(
                        "%d분 %d초",
                        fieldTrainingList.get(fieldTrainingList.size - 1) / 60,
                        fieldTrainingList.get(fieldTrainingList.size - 1) % 60
                    )
                )
            }
            if (datelist.size == 1) {

                var date1 = findViewById<TextView>(R.id.date1)
                date1.setText(datelist.get(datelist.size - 1))
            }

            if(legTuckList.size==1) {
                //레그턱
                var leg1 = findViewById<TextView>(R.id.legrecord1)
                leg1.setText(legTuckList.get(legTuckList.size - 1).toString())
            }

            if(shuttleRunList.size==1) {
                //달리기
                var run1 = findViewById<TextView>(R.id.runrecord1)
                run1.setText(
                    String.format(
                        "%d분 %d초",
                        shuttleRunList.get(shuttleRunList.size - 1) / 60,
                        shuttleRunList.get(shuttleRunList.size - 1) % 60
                    )
                )
            }

            if(fieldTrainingList.size==1) {
                //전장순환
                var circuit1 = findViewById<TextView>(R.id.circuitrecord1)
                circuit1.setText(
                    String.format(
                        "%d분 %d초",
                        fieldTrainingList.get(fieldTrainingList.size - 1) / 60,
                        fieldTrainingList.get(fieldTrainingList.size - 1) % 60
                    )
                )
            }
        }
    }
    fun showTwoButtonDialogMessage(title: String, body: String, callBack: (Int) -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.show()
    }

    fun showDialogMessage(title: String, body: String, callBack: () -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack()}
        builder.show()
    }

    override fun onBackPressed() {
        showTwoButtonDialogMessage("알림", "Mili Meter를 종료하시겠습니까?"){
            when(it){
                -1 -> {
                    finishAffinity()
                    exitProcess(0)
                }
            }
        }
    }
}





















package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.DataUtil
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.EmptyCoroutineContext


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        //그래프
        result_graph()

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
                date1.setText(datelist.get(datelist.size - 3))
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
}





















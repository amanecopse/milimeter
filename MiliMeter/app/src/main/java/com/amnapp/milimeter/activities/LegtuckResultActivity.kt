package com.amnapp.milimeter.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.TrainingValueFormatter
import com.amnapp.milimeter.UserData
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import org.w3c.dom.Text

class LegtuckResultActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leg_result)

        //버튼
        val CurrentButton = findViewById<Button>(R.id.all)
        CurrentButton.setOnClickListener {
            val currentintent = Intent(this, ResultActivity::class.java)
            startActivity(currentintent)
        }
        //달리기
        val runButton = findViewById<Button>(R.id.run)
        runButton.setOnClickListener {
            val runintent = Intent(this, RunningResultActivity::class.java)
            startActivity(runintent)
        }
        //전장순환
        val circuitButton = findViewById<Button>(R.id.circuit)
        circuitButton.setOnClickListener {
            val circuitintent = Intent(this, CircuitResultActivity::class.java)
            startActivity(circuitintent)
        }


        //등급하고 그래프 표시
        showGrade()

        //그래프 적용
        findViewById<LineChart>(R.id.legChart).apply{
            graph()}
    }

    private fun showGrade() {
        val cm = ChartManager()

        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            7
        ) { docs, lineDataSets, dateList ->

            val legTuckList = mutableListOf<Int>()
            for (doc in docs) {
                doc.data?.get(ChartManager.LEG_TUCK)?.let { score ->
                    legTuckList.add(score.toString().toInt())

                }
                //이미지로 저장할예정
                if (legTuckList.size > 0) {
                    val grade = cm.calculateGrade(legTuckList.get(legTuckList.size - 1), "LEG_TUCK")
                    if (grade == 10f) findViewById<TextView>(R.id.expert).setText("<-")
                    else if (grade == 9f) findViewById<TextView>(R.id.grade1).setText("<-")
                    else if (grade == 8f) findViewById<TextView>(R.id.grade2).setText("<-")
                    else if (grade == 7f) findViewById<TextView>(R.id.grade3).setText("<-")
                    else if (grade == 6f) findViewById<TextView>(R.id.grade4).setText("<-")
                    else if (grade == 5f) findViewById<TextView>(R.id.grade5).setText("<-")
                    else if (grade == 4f) findViewById<TextView>(R.id.grade6).setText("<-")
                    else if (grade == 3f) findViewById<TextView>(R.id.grade7).setText("<-")
                    else if (grade == 2f) findViewById<TextView>(R.id.grade8).setText("<-")
                    else findViewById<TextView>(R.id.grade9).setText("<-")
                } else {
                    continue
                }
            }
        }
        //바그래프 저장
    }

    private fun graph() {
        val linechart = findViewById<LineChart>(R.id.legChart)
        val cm = ChartManager()

        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            7
        ) { docs, lineDataSets, dateList ->

            //데이터 없을때
            if(dateList.size>1) {
                val legTuckEntry = mutableListOf<Entry>()//
                val datelist = ArrayList<String>() //날짜 x축값
                var index = 0


                val xAxis = linechart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = Color.BLACK//x축 색깔
                xAxis.valueFormatter = (TrainingValueFormatter(dateList))
                xAxis.granularity = 1f
                xAxis.isGranularityEnabled = true

                val yLAxis = linechart.axisLeft//y축
                yLAxis.axisMaximum = 25f //y축최대
                yLAxis.axisMinimum = 0f  //

                linechart.axisRight.isEnabled = false
                for (doc in docs) {
                    //데이터 엔트리 추가
                    doc.data?.get(ChartManager.LEG_TUCK)?.let { score ->
                        legTuckEntry.add(Entry(index.toFloat(), score.toString().toFloat()))
                    }
                    doc.data?.get(ChartManager.DATE)?.let { score ->
                        datelist.add(score.toString())

                    }
                    index += 1
                }

                val linedataset = LineDataSet(legTuckEntry, "레그턱")
                linedataset.setColor(Color.BLUE)
                linedataset.setDrawValues(false)
                linedataset.setLineWidth(2F)
                linedataset.setCircleRadius(6F)
                linechart.data = LineData(linedataset)//데이터 입력
                linechart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                linechart.axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                linechart.isDoubleTapToZoomEnabled = false
                linechart.setDrawGridBackground(false)

                linechart.animateY(2000, Easing.EaseInCubic)
                linechart.invalidate()
            }

            //아무것도 없으면 안함
            else{

            }
        }


    }


}
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

class CircuitResultActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_result)

        showGrade()
        //버튼
        val CurrentButton = findViewById<Button>(R.id.all)
        CurrentButton.setOnClickListener {
            val currentintent = Intent(this, ResultActivity::class.java)
            startActivity(currentintent)
        }
        //레그턱
        val legButton = findViewById<Button>(R.id.legtuckbutton)
        legButton.setOnClickListener {
            val legintent = Intent(this, LegtuckResultActivity::class.java)
            startActivity(legintent)
        }
        //240m가는 버튼
        val runButton = findViewById<Button>(R.id.run)
        runButton.setOnClickListener {
            val runintent = Intent(this, RunningResultActivity::class.java)
            startActivity(runintent)

        }
        showGrade()


        //그래프 적용
        findViewById<LineChart>(R.id.circuitChart).apply{
            graph()}

    }
    private fun showGrade() {
        val cm = ChartManager()

        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            7
        ) { docs, lineDataSets, dateList ->

            val FieldTrainingList = mutableListOf<Int>()
            for (doc in docs) {
                doc.data?.get(ChartManager.FIELD_TRAINING)?.let { score ->
                    FieldTrainingList.add(score.toString().toInt())

                }
                //이미지로 저장할예정
                if (FieldTrainingList.size > 0) {
                    val grade = cm.calculateGrade(FieldTrainingList.get(FieldTrainingList.size - 1), "FIELD_TRAINING")
                    if(grade == 10f) findViewById<TextView>(R.id.expert).setText("<-")
                    else if (grade == 9f) findViewById<TextView>(R.id.grade1).setText("<-")
                    else if (grade == 8f) findViewById<TextView>(R.id.grade2).setText("<-")
                    else if (grade == 7f) findViewById<TextView>(R.id.grade3).setText("<-")
                    else if (grade == 6f) findViewById<TextView>(R.id.grade4).setText("<-")
                    else if (grade == 5f) findViewById<TextView>(R.id.grade5).setText("<-")
                    else if (grade == 4f) findViewById<TextView>(R.id.grade6).setText("<-")
                    else if (grade == 3f) findViewById<TextView>(R.id.grade7).setText("<-")
                    else if (grade == 2f) findViewById<TextView>(R.id.grade8).setText("<-")
                    else findViewById<TextView>(R.id.grade9).setText("<-")
                }
                else {
                    continue
                }
            }
        }
        //바그래프 저장
    }

    private fun graph() {
        val linechart = findViewById<LineChart>(R.id.circuitChart)
        val cm = ChartManager()

        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            7
        ) { docs, lineDataSets, dateList ->

            //데이터 없을때
            if (dateList.size > 1) {
                val circuit = mutableListOf<Entry>()
                var index = 0
                for (doc in docs) {
                    doc.data?.get(ChartManager.FIELD_TRAINING)?.let { score ->
                        circuit.add(
                            Entry(index.toFloat(), cm.calculateGrade(score.toString().toInt(), ChartManager.LEG_TUCK)
                            )
                        )

                    }
                    index += 1

                }
                val circuitdataset =LineDataSet(circuit,"전장순환")
                val linedataset = mutableListOf<LineDataSet>(circuitdataset)
                cm.makeLineChart(linechart,linedataset,dateList)
                val yValue = arrayListOf<String>(
                    "",
                    "5분3초이상",
                    "5분3초",
                    "4분42초",
                    "4분21초",
                    "4분",
                    "3분39초",
                    "3분18초",
                    "2분 57초",
                    "2분36초",
                    "2분 15초"
                )

                val yRAxis = linechart.axisRight
                yRAxis.axisMaximum=10f //y축최대
                yRAxis.axisMinimum=0f  //y축최소
                yRAxis.valueFormatter =(TrainingValueFormatter(yValue))
                yRAxis.granularity =1f
                yRAxis.labelCount=10
                yRAxis.textColor = Color.BLACK

                linechart.axisLeft.isEnabled=false

            }
                        /*
                val lineChart = findViewById<LineChart>(R.id.legChart)
                val yValue = arrayListOf<String>(
                    "",
                    "5분3초이상",
                    "5분3초",
                    "4분42초",
                    "4분21초",
                    "4분",
                    "3분39초",
                    "3분18초",
                    "2분 57초",
                    "2분36초",
                    "2분 15초"
                )
                var index = 0
                val circuit = ArrayList<Entry>()
                val datelist=ArrayList<String>()
                val yRAxis = lineChart.axisRight



                val xAxis = linechart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = Color.BLACK//x축 색깔
                xAxis.valueFormatter = (TrainingValueFormatter(dateList))
                xAxis.granularity = 1f
                xAxis.isGranularityEnabled = true


                yRAxis.axisMaximum = 303f //y축최대
                yRAxis.axisMinimum = 0f  //y축최소
                yRAxis.valueFormatter = (TrainingValueFormatter(yValue))
                yRAxis.granularity = 1f
                yRAxis.labelCount = 10
                yRAxis.textColor = Color.BLACK
                for (doc in docs) {
                    doc.data?.get(ChartManager.FIELD_TRAINING)?.let { score ->

                        if(303-score.toString().toFloat()>=0)
                        circuit.add(
                            Entry(index.toFloat(), 535f - score.toString().toFloat()),

                            )
                        //더크면
                        else{
                            Entry(index.toFloat(), 400f)
                        }

                    }
                    doc.data?.get(ChartManager.DATE)?.let { score ->
                        datelist.add(score.toString())

                    }
                    index += 1

                }
                val linedataset = LineDataSet(circuit, "전장순환")
                linedataset.setColor(Color.GREEN)
                linedataset.setDrawValues(false)
                linedataset.setLineWidth(2F)
                linedataset.setCircleRadius(6F)
                linechart.data = LineData(linedataset)//데이터 입력
                linechart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                linechart.axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                linechart.isDoubleTapToZoomEnabled = false
                linechart.setDrawGridBackground(false)
                */
                        else {

                        }
                    }
                }

            }







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
import com.amnapp.milimeter.databinding.ActivityRunningResultBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class RunningResultActivity: CustomThemeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var binding: ActivityRunningResultBinding
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityRunningResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 창닫기 (바로 결과창으로)
        binding.cancelIb.setOnClickListener {
            val intentBack = Intent(this, ResultActivity::class.java)
            startActivity(intentBack)
            finish()
        }
        // 뒤로가기 (결과창으로)
        binding.backIb.setOnClickListener {
            finish()
        }

        //버튼
        val CurrentButton = findViewById<Button>(R.id.all)
        CurrentButton.setOnClickListener {
            val currentintent = Intent(this, ResultActivity::class.java)
            startActivity(currentintent)
        }
        //레그턱 버튼
        val legButton = findViewById<Button>(R.id.legtuckbutton)
        legButton.setOnClickListener {
            val legintent = Intent(this, LegtuckResultActivity::class.java)
            startActivity(legintent)
        }
        //전장순환
        val circuitButton = findViewById<Button>(R.id.circuit)
        circuitButton.setOnClickListener {
            val circuitintent = Intent(this, CircuitResultActivity::class.java)
            startActivity(circuitintent)
        }
        findViewById<LineChart>(R.id.runChart).apply {
            graph()
        }
        showGrade()

    }

    private fun showGrade() {
        val cm = ChartManager()

        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            30
        ) { docs, lineDataSets, dateList ->

            val list = mutableListOf<Int>()
            for (doc in docs) {
                doc.data?.get(ChartManager.SHUTTLE_RUN)?.let { score ->
                    list.add(score.toString().toInt())

                }
                //이미지로 저장할예정
                if (list.size > 0) {
                    val grade = cm.calculateGrade(list.get(list.size-1), ChartManager.SHUTTLE_RUN)
                    if (grade == 10f) findViewById<TextView>(R.id.rexpert).setText("<--")
                    else if (grade == 9f) findViewById<TextView>(R.id.rgrade1).setText("<--")
                    else if (grade == 8f) findViewById<TextView>(R.id.rgrade2).setText("<--")
                    else if (grade == 7f) findViewById<TextView>(R.id.rgrade3).setText("<--")
                    else if (grade == 6f) findViewById<TextView>(R.id.rgrade4).setText("<--")
                    else if (grade == 5f) findViewById<TextView>(R.id.rgrade5).setText("<--")
                    else if (grade == 4f) findViewById<TextView>(R.id.rgrade6).setText("<--")
                    else if (grade == 3f) findViewById<TextView>(R.id.rgrade7).setText("<--")
                    else if (grade == 2f) findViewById<TextView>(R.id.rgrade8).setText("<--")
                    else findViewById<TextView>(R.id.rgrade9).setText("<--")
                } else {
                    continue
                }
            }
        }
        //바그래프 저장
    }

    private fun graph() {
        val linechart = findViewById<LineChart>(R.id.runChart)
        val cm = ChartManager()

        cm.loadTrainingRecordNDaysAgo(
            UserData.getInstance(),
            cm.getCurrentDateBasedOnFormat(),
            7
        ) { docs, lineDataSets, dateList ->

            //데이터 없을때
            if (dateList.size > 1) {
                val run = mutableListOf<Entry>()
                var index = 0
                for (doc in docs) {
                    doc.data?.get(ChartManager.SHUTTLE_RUN)?.let { score ->
                        run.add(
                            Entry(
                                index.toFloat(),
                                cm.calculateGrade(score.toString().toInt(), ChartManager.SHUTTLE_RUN)
                            )
                        )

                    }
                    index += 1

                }
                val rundataset = LineDataSet(run, "240m")
                rundataset.setColors(Color.RED)
                val linedataset = mutableListOf<LineDataSet>(rundataset)
                cm.makeLineChart(linechart, linedataset, dateList)
                val yValue = arrayListOf<String>(
                    "",
                    "1분50초이상",
                    "1분50초",
                    "1분44초",
                    "1분38초",
                    "1분32초",
                    "1분26초",
                    "1분20초",
                    "1분14초",
                    "1분8초",
                    "1분2초"
                )

                val yRAxis = linechart.axisRight
                yRAxis.axisMaximum = 10f //y축최대
                yRAxis.axisMinimum = 0f  //y축최소
                yRAxis.valueFormatter = (TrainingValueFormatter(yValue))
                yRAxis.granularity = 1f
                yRAxis.labelCount = 10
                yRAxis.textColor = Color.BLACK

                linechart.axisLeft.isEnabled = false

            }

            //그래프

            //등급표시
        }
    }
}
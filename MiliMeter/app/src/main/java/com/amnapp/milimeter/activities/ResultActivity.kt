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
import com.github.mikephil.charting.data.LineDataSet

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        current_table()
        result_graph()

        //레그턱 가는 버튼
        var legButton =findViewById<Button>(R.id.legtuckbutton)
        legButton.setOnClickListener{
            val legintent =Intent(this, LegtuckResultActivity::class.java)
            startActivity(legintent)
        }
        //240m가는 버튼
        var runButton =findViewById<Button>(R.id.run)
        runButton.setOnClickListener{
            val runintent =Intent(this,RunningResultActivity::class.java)
            startActivity(runintent)
        }
        //전장순환
        var circuitButton=findViewById<Button>(R.id.circuit)
        circuitButton.setOnClickListener{
            val circuitintent =Intent(this,CircuitResultActivity::class.java)
            startActivity(circuitintent)
        }



        //홈버튼
        var homebtn =findViewById<Button>(R.id.homeBt)
        homebtn.setOnClickListener{
            val homeintent =Intent(this, HomeActivity::class.java)
            startActivity(homeintent)
        }
        /*
        //바디버튼 //결과는 지금 이창
        var bodyBtn =findViewById<Button>(this,)
        bodyByn.setOnClickListner{
               val bodyintent =Intent{this, }
               starActivity(homeintent)
            }
            */
        //목표버튼
        var goalBtn =findViewById<Button>(R.id.goalBt)
        goalBtn.setOnClickListener{
            val goalintent =Intent(this,GoalActivity::class.java)
            startActivity(goalintent)
        }

        //세팅버튼
        var optionBtn =findViewById<Button>(R.id.settingBt)
        optionBtn.setOnClickListener{
            val optionintent = Intent(this,SettingActivity::class.java)
            startActivity(optionintent)
        }



    }
    //그래프 생성
    private fun result_graph(){
        var linechart=findViewById<LineChart>(R.id.lineChart)
        val cm = ChartManager()
        cm.loadTrainingRecordNDaysAgo(UserData.getInstance(), cm.getCurrentDateBasedOnFormat(), 7){ docs, lineDataSets, dateList ->
            cm.makeLineChart(linechart, lineDataSets, dateList)
        }
    }

    private fun current_table(){//동적 데이터 입력

     val xvalue=ArrayList<String>()//날짜
        xvalue.add("5월1주")
        xvalue.add("5월2주")
        xvalue.add("5월3주")
        xvalue.add("5월4주")
        xvalue.add("6월1주")
        xvalue.add("6월2주")

        //데이터 리스트화
        val legtuck = mutableListOf<Int>()
        val running = mutableListOf<Int>()
        val circuit = mutableListOf<Int>()


        for(data in DataUtil.getData()){
            legtuck.add(data.legtuck)
            running.add(data.running)
            circuit.add(data.circuit)

        }


        //날짜
        var date1=findViewById<TextView>(R.id.date1)
        date1.setText(xvalue.get(xvalue.size-3))
        var date2=findViewById<TextView>(R.id.date2)
        date2.setText(xvalue.get(xvalue.size-2))
        var date3=findViewById<TextView>(R.id.date3)
        date3.setText(xvalue.get(xvalue.size-1))

        //레그턱
        var leg1=findViewById<TextView>(R.id.legrecord1)
        leg1.setText(legtuck.get(legtuck.size-3).toString())
        var leg2=findViewById<TextView>(R.id.legrecord2)
        leg2.setText(legtuck.get(legtuck.size-2).toString())
        var leg3=findViewById<TextView>(R.id.legrecord3)
        leg3.setText(legtuck.get(legtuck.size-1).toString())


        //240m
        var run1 =findViewById<TextView>(R.id.runrecord1)
        run1.setText(String.format("%d분 %d초",running.get(running.size-3)/60,running.get(running.size-3)%60))
        var run2 =findViewById<TextView>(R.id.runrecord2)
        run2.setText(String.format("%d분 %d초",running.get(running.size-2)/60,running.get(running.size-2)%60))
        var run3 =findViewById<TextView>(R.id.runrecord3)
        run3.setText(String.format("%d분 %d초",running.get(running.size-1)/60,running.get(running.size-1)%60))

        //전장 순환
        var circuit1 =findViewById<TextView>(R.id.circuitrecord1)
        circuit1.setText(String.format("%d분 %d초",circuit.get(circuit.size-3)/60,running.get(circuit.size-3)%60))
        var circuit2 =findViewById<TextView>(R.id.circutrecord2)
        circuit2.setText(String.format("%d분 %d초",circuit.get(circuit.size-2)/60,circuit.get(circuit.size-2)%60))
        var circuit3 =findViewById<TextView>(R.id.circuitrecord3)
        circuit3.setText(String.format("%d분 %d초",circuit.get(circuit.size-1)/60,circuit.get(circuit.size-1)%60))
    }

}






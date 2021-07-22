package com.amnapp.milimeter.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.amnapp.milimeter.R

class CurrentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current)

        setLineChartData()
        Current_table()


        //레그턱 가는 버튼
        var legButton =findViewById<Button>(R.id.legtuckbutton)
        legButton.setOnClickListener{
            val legintent =Intent(this, Legtuck_Current_Activity::class.java)
            startActivity(legintent)
        }
        //240m가는 버튼
        var runButton =findViewById<Button>(R.id.run)
        runButton.setOnClickListener{
            val runintent =Intent(this,Running_Current_Activity::class.java)
            startActivity(runintent)
        }
        //전장순환
        var circuitButton=findViewById<Button>(R.id.circuit)
        circuitButton.setOnClickListener{
            val circuitintent =Intent(this,Circuit_Current_Activity::class.java)
            startActivity(circuitintent)
        }




    }
    //그래프 생성
    fun setLineChartData(){
        var linechart =findViewById<LineChart>(R.id.lineChart)
        linechart.description.text=""
        linechart.axisLeft.isEnabled=false

        linechart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        linechart.axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        linechart.invalidate()
        //x축설정
        val xvalue=ArrayList<String>()//날짜
        xvalue.add("5월1주")
        xvalue.add("5월2주")
        xvalue.add("5월3주")
        xvalue.add("5월4주")
        xvalue.add("6월1주")
        xvalue.add("6월2주")

        //y축설정
        val yvalue =ArrayList<String>()
        yvalue.add("9등급")
        yvalue.add("8등급")
        yvalue.add("7등급")
        yvalue.add("6등급")
        yvalue.add("5등급")
        yvalue.add("4등급")
        yvalue.add("3등급")
        yvalue.add("2등급")
        yvalue.add("1등급")
        yvalue.add("특급")




        // 기록이 저장될 리스트
        val legtuck = mutableListOf<Int>()
        val running = mutableListOf<Int>()
        val circuit = mutableListOf<Int>()


        for(data in DataUtil.getData()){
            legtuck.add(data.legtuck)
            running.add(data.running)
            circuit.add(data.circuit)

        }

        //레그턱
        val leg_entry = ArrayList<Entry>()
        for(i in 0..legtuck.size-1){
            var index=i
            //9등급
            if (legtuck[i]<3){
                leg_entry.add(Entry(index.toFloat(),1f))
            }
            //8등급
            else if (legtuck[i]<5){
                leg_entry.add(Entry(index.toFloat(),2f))
            }
            //7등급
            else if (legtuck[i]<8){
                leg_entry.add(Entry(index.toFloat(),3f))
            }
            //6등급
            else if (legtuck[i]<10){
                leg_entry.add(Entry(index.toFloat(),4f))
            }
            //5등급
            else if (legtuck[i]<12){
                leg_entry.add(Entry(index.toFloat(),5f))
            }
            //4등급
            else if (legtuck[i]<14){
                leg_entry.add(Entry(index.toFloat(),6f))
            }
            //3등급
            else if (legtuck[i]<16){
                leg_entry.add(Entry(index.toFloat(),7f))
            }
            //2등급
            else if (legtuck[i]<18){
                leg_entry.add(Entry(index.toFloat(),8f))
            }
            //1등급
            else if (legtuck[i]<20){
                leg_entry.add(Entry(index.toFloat(),9f))
            }
            else{
                leg_entry.add(Entry(index.toFloat(),10f))
            }

        }

        val legtuck_dataset =LineDataSet(leg_entry,"레그턱 등급")
        legtuck_dataset.valueTextColor=Color.BLUE
        legtuck_dataset.setColor(Color.BLUE)

        //240m달리기
        val run_entry = ArrayList<Entry>()
        for(i in 0..running.size-1) {
            var index = i
            //9등급
            if (running[i] > 110) {
                run_entry.add(Entry(index.toFloat() , 1f))
            }
            //8등급
            else if (running[i] > 104) {
                run_entry.add(Entry(index.toFloat() , 2f))
            }
            //7등급
            else if (running[i] > 98) {
                run_entry.add(Entry(index.toFloat() , 3f))
            }
            //6등급
            else if (running[i] > 92) {
                run_entry.add(Entry(index.toFloat() , 4f))
            }
            //5등급
            else if (running[i] > 86) {
                run_entry.add(Entry(index.toFloat() , 5f))
            }
            //4등급
            else if (running[i] > 80) {
                run_entry.add(Entry(index.toFloat() , 6f))
            }
            //3등급
            else if (running[i] > 74) {
                run_entry.add(Entry(index.toFloat() , 7f))
            }
            //2등급
            else if (running[i] > 68) {
                run_entry.add(Entry(index.toFloat() , 8f))
            }
            //1등급
            else if (running[i] > 62) {
                run_entry.add(Entry(index.toFloat() , 9f))
            }
            //특급
            else {
                run_entry.add(Entry(index.toFloat() , 10f))
            }
        }
        val running_dataset =LineDataSet(run_entry,"240m왕복달리기")
        running_dataset.valueTextColor=Color.RED
        running_dataset.setColor(Color.RED)


        //전장순환운동
        val circuit_entry = ArrayList<Entry>()
        for(i in 0..circuit.size-1) {
            var index = i
            //9등급
            if (circuit[i] > 303) {
                circuit_entry.add(Entry(index.toFloat() , 1f))
            }
            //8등급
            else if (circuit[i] > 282) {
                circuit_entry.add(Entry(index.toFloat() , 2f))
            }
            //7등급
            else if (circuit[i] > 261) {
                circuit_entry.add(Entry(index.toFloat() , 3f))
            }
            //6등급
            else if (circuit[i] > 240) {
                circuit_entry.add(Entry(index.toFloat() , 4f))
            }
            //5등급
            else if (circuit[i] > 219) {
                circuit_entry.add(Entry(index.toFloat() , 5f))
            }
            //4등급
            else if (circuit[i] > 198) {
                circuit_entry.add(Entry(index.toFloat() , 6f))
            }
            //3등급
            else if (running[i] > 177) {
                circuit_entry.add(Entry(index.toFloat() , 7f))
            }
            //2등급
            else if (running[i] > 156) {
                circuit_entry.add(Entry(index.toFloat() , 8f))
            }
            //1등급
            else if (running[i] > 135) {
                circuit_entry.add(Entry(index.toFloat() , 9f))
            }
            else {
                circuit_entry.add(Entry(index.toFloat() , 10f))
            }
        }
        val circuit_dataset =LineDataSet(circuit_entry,"전장순환 운동")
        circuit_dataset.valueTextColor=Color.GREEN
        circuit_dataset.setColor(Color.GREEN)

        //데이터 차트
        val data = LineData(legtuck_dataset,running_dataset,circuit_dataset)


        linechart.data=data
      //터지 못하게하기
        var x:XAxis =linechart.getXAxis()
        var y:YAxis =linechart.getAxisRight()

        //x축값 설정
        x.position=XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)
        x.labelCount =4
        x.granularity=1f
        x.isGranularityEnabled=true
        x.valueFormatter =(MyValueFormatter(xvalue))

        //y축값
        y.axisMaximum=10f //y축최대
        y.axisMinimum=0f  //y축최소
        y.valueFormatter =(MyValueFormatter(yvalue))
        y.granularity =1f
        y.labelCount=4
    }



    //축 값 설정
    class MyValueFormatter(var xvalue: ArrayList<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.toString()
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            if(value.toInt() >=0 && value.toInt() <=xvalue.size-1){
                return xvalue[value.toInt()]
            }else{
                return ("").toString()
            }
        }

    }

    fun Current_table(){//동적 데이터 입력

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






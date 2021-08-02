package com.amnapp.milimeter

import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChartManager {
    interface UICallBack{
        fun whatToDo()
        fun whatToDoWithLineDataSets(lineDataSets: MutableList<LineDataSet>, dateList: ArrayList<String>)
        fun whatToDoWithDocuments(docs: MutableList<DocumentSnapshot>)
    }

    fun makeLineChart(lineChart: LineChart, lineDataSets: MutableList<LineDataSet>, dateList: ArrayList<String>){
        val colorSet = arrayOf(Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW)
        var colorIndex = 0
        for(lineDataSet in lineDataSets){
            lineDataSet.setLineWidth(2F)
            lineDataSet.setCircleRadius(6F)
            lineDataSet.setCircleColor(colorSet[colorIndex])
            lineDataSet.circleHoleColor = colorSet[colorIndex]
            lineDataSet.setColor(colorSet[colorIndex])
            lineDataSet.setDrawCircleHole(true)
            lineDataSet.setDrawCircles(true)
            lineDataSet.setDrawHorizontalHighlightIndicator(false)
            lineDataSet.setDrawHighlightIndicators(false)
            lineDataSet.setDrawValues(false)
            colorIndex = if(colorIndex==5) 0 else colorIndex+1
        }
        val data = LineData(lineDataSets as List<ILineDataSet>?)
        lineChart.data = data

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.BLACK
        //xAxis.textSize = R.dimen.smallText.toFloat()
        xAxis.setDrawGridLines(false)
        xAxis.labelCount =4
        xAxis.granularity=1f
        xAxis.isGranularityEnabled=true
        xAxis.valueFormatter =(TrainingValueFormatter(dateList))

        val yValue = arrayListOf<String>("","9등급", "8등급", "7등급", "6등급", "5등급", "4등급", "3등급", "2등급", "1등급", "특급")

        val yRAxis = lineChart.axisRight
        yRAxis.axisMaximum=10f //y축최대
        yRAxis.axisMinimum=0f  //y축최소
        yRAxis.valueFormatter =(TrainingValueFormatter(yValue))
        yRAxis.granularity =1f
        yRAxis.labelCount=10
        yRAxis.textColor = Color.BLACK
        //yRAxis.textSize = R.dimen.smallText.toFloat()


        val yLAxis = lineChart.axisLeft
        yLAxis.axisMaximum=10f //y축최대
        yLAxis.axisMinimum=0f  //y축최소
        yLAxis.valueFormatter =(TrainingValueFormatter(yValue))
        yLAxis.granularity =1f
        yLAxis.labelCount=10
        yLAxis.textColor = Color.BLACK
        //yLAxis.textSize = R.dimen.smallText.toFloat()




        lineChart.axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        lineChart.axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.setDrawGridBackground(false)

        lineChart.animateY(2000, Easing.EaseInCubic)
        lineChart.invalidate()
    }

    fun loadTrainingRecordNDaysAgo(userData: UserData,date: String,howLongAgo: Int, callBack: UICallBack){
        val ref = userData.id?.let {
            Firebase.firestore.collection(USERS).document(it)
                .collection(TRAINING_RECORDS)
        }
        ref?.get()?.addOnSuccessListener {
            if(!it.isEmpty){//training records 컬렉션이 존재함
                ref.whereLessThanOrEqualTo(DATE, date)//기준일로부터
                    .whereGreaterThan(DATE, calculateDate(date, -howLongAgo))//n일전까지의 자료를
                    .orderBy(DATE)// 일자순으로 가져옴
                    .get()
                    .addOnSuccessListener { queryResult ->
                        var index = 0
                        val docs = queryResult.documents
                        val dateList = ArrayList<String>()
                        val legTuckEntryList = ArrayList<Entry>()
                        val shuttleRunEntryList = ArrayList<Entry>()
                        val fieldTrainingEntryList = ArrayList<Entry>()

                        for(doc in docs){
                            dateList.add(doc.data?.get(DATE) as String)
                            doc.data?.get(LEG_TUCK)?.let { score->
                                legTuckEntryList.add(
                                    Entry(index.toFloat(),calculateGrade(score.toString().toInt(), LEG_TUCK))
                                )
                            }
                            doc.data?.get(SHUTTLE_RUN)?.let { score->
                                shuttleRunEntryList.add(
                                    Entry(index.toFloat(),calculateGrade(score.toString().toInt(), SHUTTLE_RUN))
                                )
                            }
                            doc.data?.get(FIELD_TRAINING)?.let { score->
                                fieldTrainingEntryList.add(
                                    Entry(index.toFloat(),calculateGrade(score.toString().toInt(), FIELD_TRAINING))
                                )
                            }
                            index++
                        }
                        val legTuckLineDataSet = LineDataSet(legTuckEntryList,"레그턱")
                        val shuttleRunLineDataSet = LineDataSet(shuttleRunEntryList,"240m 왕복달리기")
                        val fieldTrainingLineDataSet = LineDataSet(fieldTrainingEntryList,"전장순환훈련")
                        val lineDataSets = mutableListOf<LineDataSet>(
                            legTuckLineDataSet, shuttleRunLineDataSet, fieldTrainingLineDataSet
                        )
                        callBack.whatToDoWithLineDataSets(lineDataSets, dateList)
                        callBack.whatToDoWithDocuments(docs)
                    }
            }
        }
    }

    fun loadTrainingRecordNDaysAgo(
        userData: UserData,
        date: String, howLongAgo: Int,
        callBack: (
            docs: MutableList<DocumentSnapshot>, lineDataSets: MutableList<LineDataSet>, dateList: ArrayList<String>
                ) -> Unit
    ){
        val ref = userData.id?.let {
            Firebase.firestore.collection(USERS).document(it)
                .collection(TRAINING_RECORDS)
        }
        ref?.get()?.addOnSuccessListener {
            if(!it.isEmpty){//training records 컬렉션이 존재함
                ref.whereLessThanOrEqualTo(DATE, date)//기준일로부터
                    .whereGreaterThan(DATE, calculateDate(date, -howLongAgo))//n일전까지의 자료를
                    .orderBy(DATE)// 일자순으로 가져옴
                    .get()
                    .addOnSuccessListener { queryResult ->
                        var index = 0
                        val docs = queryResult.documents
                        val dateList = ArrayList<String>()
                        val legTuckEntryList = ArrayList<Entry>()
                        val shuttleRunEntryList = ArrayList<Entry>()
                        val fieldTrainingEntryList = ArrayList<Entry>()

                        for(doc in docs){
                            dateList.add(doc.data?.get(DATE) as String)
                            doc.data?.get(LEG_TUCK)?.let { score->
                                legTuckEntryList.add(
                                    Entry(index.toFloat(),calculateGrade(score.toString().toInt(), LEG_TUCK))
                                )
                            }
                            doc.data?.get(SHUTTLE_RUN)?.let { score->
                                shuttleRunEntryList.add(
                                    Entry(index.toFloat(),calculateGrade(score.toString().toInt(), SHUTTLE_RUN))
                                )
                            }
                            doc.data?.get(FIELD_TRAINING)?.let { score->
                                fieldTrainingEntryList.add(
                                    Entry(index.toFloat(),calculateGrade(score.toString().toInt(), FIELD_TRAINING))
                                )
                            }
                            index++
                        }
                        val legTuckLineDataSet = LineDataSet(legTuckEntryList,"레그턱")
                        val shuttleRunLineDataSet = LineDataSet(shuttleRunEntryList,"240m 왕복달리기")
                        val fieldTrainingLineDataSet = LineDataSet(fieldTrainingEntryList,"전장순환훈련")
                        val lineDataSets = mutableListOf<LineDataSet>(
                            legTuckLineDataSet, shuttleRunLineDataSet, fieldTrainingLineDataSet
                        )
                        callBack(docs, lineDataSets, dateList)
                    }
            }
        }
    }

    fun updateTrainingRecord(userData: UserData, date: String, record: HashMap<String, String>, callBack: UICallBack){
        userData.id?.let { id ->
            Firebase.firestore.collection(USERS)
                .document(id)
                .collection(TRAINING_RECORDS)
                .document(date)
                .set(record, SetOptions.merge())
                .addOnSuccessListener {
                    callBack.whatToDo()
                }
        }
    }

    fun calculateDate(date: String, dayDifference: Int): String{
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd").parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = Date(simpleDateFormat.time)
        calendar.add(Calendar.DAY_OF_MONTH, dayDifference)
        return SimpleDateFormat("yyyy.MM.dd").format(calendar.time)
    }

    fun calculateGrade(score: Int, course: String): Float{
        if(course == LEG_TUCK){
            if (score<3) return 1f //9등급
            else if (score<5) return 2f //8등급
            else if (score<8) return 3f //7등급
            else if (score<10) return 4f //6등급
            else if (score<12) return 5f //5등급
            else if (score<14) return 6f //4등급
            else if (score<16) return 7f //3등급
            else if (score<18) return 8f //2등급
            else if (score<20) return 9f //1등급
            else return 10f //특급
        }
        else if(course == SHUTTLE_RUN){
            if (score>110) return 1f //9등급
            else if (score>104) return 2f //8등급
            else if (score>98) return 3f //7등급
            else if (score>92) return 4f //6등급
            else if (score>86) return 5f //5등급
            else if (score>80) return 6f //4등급
            else if (score>74) return 7f //3등급
            else if (score>68) return 8f //2등급
            else if (score>62) return 9f //1등급
            else return 10f //특급
        }
        else{//전장순환훈련
            if (score>303) return 1f //9등급
            else if (score>282) return 2f //8등급
            else if (score>261) return 3f //7등급
            else if (score>240) return 4f //6등급
            else if (score>219) return 5f //5등급
            else if (score>198) return 6f //4등급
            else if (score>177) return 7f //3등급
            else if (score>156) return 8f //2등급
            else if (score>135) return 9f //1등급
            else return 10f //특급
        }
    }

    fun getCurrentDateBasedOnFormat(): String{
        return SimpleDateFormat("yyyy.MM.dd").format(Date())
    }

    companion object{
        const val USERS = "users"
        const val TRAINING_RECORDS = "training records"
        const val DATE = "date"
        const val LEG_TUCK = "legTuck"
        const val SHUTTLE_RUN = "shuttleRun"
        const val FIELD_TRAINING = "fieldTraining"
        const val WEIGHT = "weight"


    }
}

class TrainingValueFormatter(var xValue: ArrayList<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        if(value.toInt() >=0 && value.toInt() <=xValue.size-1){
            return xValue[value.toInt()]
        }else{
            return ("").toString()
        }
    }
}
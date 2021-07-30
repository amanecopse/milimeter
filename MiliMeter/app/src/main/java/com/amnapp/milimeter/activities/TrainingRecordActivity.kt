package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityTrainingRecordBinding
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrainingRecordActivity : AppCompatActivity() {

    lateinit var binding: ActivityTrainingRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        showChart()
    }

    private fun showChart() {
        val cm = ChartManager()
        cm.loadTrainingRecordNDaysAgo(UserData.getInstance(), cm.getCurrentDateBasedOnFormat(), 7){ docs, lineDataSets, dateList ->
            cm.makeLineChart(binding.chartLc, lineDataSets, dateList)
        }
    }

    private fun initUI() {
        binding.dateTv.setOnClickListener {
            showDatePickerDialog()
        }
        binding.confirmBt.setOnClickListener {
            val myUd = UserData.getInstance()
            val currDate = binding.dateTv.text.toString()
            if(!myUd.login){
                showDialogMessage("오류", "로그인 한 뒤 기록해 주세요")
                return@setOnClickListener
            }
            else if(binding.dateTv.text.toString() == "클릭해서 입력" || binding.recordEt.text.isNullOrBlank()){
                showDialogMessage("오류", "공란을 입력해 주세요")
                return@setOnClickListener
            }
            val course = arrayOf("legTuck", "shuttleRun", "fieldTraining", "weight")
            val record = hashMapOf(
                course[binding.trainingCourseSp.selectedItemPosition] to binding.recordEt.text.toString(),
                "date" to currDate
            )
            val cm = ChartManager()
            cm.updateTrainingRecord(UserData.getInstance(),currDate,record,object: ChartManager.UICallBack{
                override fun whatToDo() {
                    showDialogMessage("완료", "운동 결과를 기록했습니다")
                    showChart()
                }

                override fun whatToDoWithLineDataSets(lineDataSets: MutableList<LineDataSet>, dateList: ArrayList<String>) {
                    return
                }

                override fun whatToDoWithDocuments(docs: MutableList<DocumentSnapshot>) {
                    return
                }

            })
        }
    }

    private fun showDatePickerDialog() {
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.dateTv.text = ""+year+"."+ String.format("%02d", month)+"."+String.format("%02d", dayOfMonth)
        }
        val year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val month = SimpleDateFormat("MM").format(Date()).toInt()
        val day = SimpleDateFormat("dd").format(Date()).toInt()
        DatePickerDialog(this, callBack,year,month,day).show()
    }

    fun showDialogMessage(title: String, body: String) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }
}
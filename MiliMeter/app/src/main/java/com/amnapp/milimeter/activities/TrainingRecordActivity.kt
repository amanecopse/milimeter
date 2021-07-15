package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityLoginBinding
import com.amnapp.milimeter.databinding.ActivityTrainingRecordBinding
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class TrainingRecordActivity : AppCompatActivity() {

    lateinit var binding: ActivityTrainingRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        binding.dateTv.setOnClickListener {
            showDatePickerDialog()
        }
        binding.confirmBt.setOnClickListener {
            val myUd = UserData.getInstance()
            if(!myUd.isLogined){
                showDialogMessage("오류", "로그인 한 뒤 기록해 주세요")
                return@setOnClickListener
            }
            else if(binding.dateTv.text.toString() == "클릭해서 입력" || binding.recordEt.text.isNullOrBlank()){
                showDialogMessage("오류", "공란을 입력해 주세요")
                return@setOnClickListener
            }
            val course = arrayOf("regTuck", "shuttleRun", "fieldTraining")
            val record = hashMapOf(
                course[binding.trainingCourseSp.selectedItemPosition] to binding.recordEt.text.toString()
            )
            myUd.indexHashCode?.let { indexHashCode ->
                Firebase.firestore.collection("users")
                    .document(indexHashCode)
                    .collection("training records")
                    .document(binding.dateTv.text.toString())
                    .set(record, SetOptions.merge())
                    .addOnSuccessListener {
                        showDialogMessage("완료", "운동 결과를 기록했습니다")
                    }
            }
        }
    }

    private fun showDatePickerDialog() {
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.dateTv.text = ""+year+"."+month+"."+dayOfMonth
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

    fun getTrainingRecord(recordDate: String, trainingCourse: String){// 날짜와 종목을 입력받아 서버에서 운동기록을 가져온다
        TODO("이것은 예시로 작성된 함수이다 " +
                "이러한 함수를 db의 운동기록값이 필요한 부분에서 사용하자")
        val myUd = UserData.getInstance()
        myUd.indexHashCode?.let { indexHashCode ->
            Firebase.firestore.collection("users")
                .document(indexHashCode)
                .collection("training records")
                .document(recordDate)
                .get()
                .addOnSuccessListener {
                    val itIsHashMap = it.data
                    val itIsScore = itIsHashMap?.get(trainingCourse)
                    TODO("itIsScore에는 해당 일자 해당 종목의 기록이 있다" +
                            "이 값을 UI의 표시하는 코드를 이곳에 작성하면된다")

                }
        }
    }
}
package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityDebugBinding
import com.amnapp.milimeter.databinding.ActivitySignInBinding
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DebugActivity : CustomThemeActivity() {

    lateinit var binding: ActivityDebugBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityDebugBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeExampleActivity.setOnClickListener {
            val intent = Intent(this, ThemeExampleActivity::class.java)
            startActivity(intent)
        }
        binding.dateBt.setOnClickListener {
            showDatePickerDialog()
        }
        binding.randomInputBt.setOnClickListener {
            if(binding.dateTv.text.isNullOrEmpty() || binding.rangeEt.text.isNullOrEmpty() || binding.idEt.text.isNullOrEmpty())
                return@setOnClickListener
            AccountManager().findUserDataById(binding.idEt.text.toString()){ resultMessage: String, querySnapshot: QuerySnapshot ->
                if(resultMessage == AccountManager.RESULT_FAILURE)
                    return@findUserDataById
                val userData = querySnapshot.documents[0].toObject<UserData>()
                inputRamdomRecordsInRange(userData!!, binding.dateTv.text.toString(), binding.rangeEt.text.toString().toInt())
            }

        }
    }

    private fun showDatePickerDialog() {
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.dateTv.text = ""+year+"."+ String.format("%02d", month+1)+"."+String.format("%02d", dayOfMonth)
        }
        val year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val month = SimpleDateFormat("MM").format(Date()).toInt()
        val day = SimpleDateFormat("dd").format(Date()).toInt()
        DatePickerDialog(this, callBack,year,month-1,day).show()
    }

    private fun inputRamdomRecordsInRange(userData: UserData, date:String, range:Int){
        var nextDate = date
        var minLegTuckRecord = 3.0f
        var maxShuttleRunRecord = 106.0f
        var maxFieldTrainingRecord = 304.0f
        for(i in 0 until range){
            inputRandomDailyRecords(userData,nextDate,minLegTuckRecord.toInt(),maxShuttleRunRecord.toInt(),maxFieldTrainingRecord.toInt())
            minLegTuckRecord += 11.0f/range
            maxShuttleRunRecord -= 30.0f/range
            maxFieldTrainingRecord -= 115.0f/range
            nextDate = calculateDate(nextDate, 1)
        }
    }

    private fun inputRandomDailyRecords(userData: UserData,
                                        date: String,
                                        minLegTuckRecord: Int = 0,// "0"~22 (0~22) / 2
                                        maxShuttleRunRecord: Int = 116,// 56~"116" (0~60) / 6
                                        maxFieldTrainingRecord: Int = 324){//114~"324" (0~210) / 23

        val maxLegTuckRecord = minLegTuckRecord + 2
        val minShuttleRunRecord = maxShuttleRunRecord - 6
        val minFieldTrainingRecord = maxFieldTrainingRecord - 23

        val random = Random()
        val regTuckRecord = (random.nextFloat()*4+minLegTuckRecord).toInt().toString()
        val shuttleRunRecord = (random.nextFloat()*12+minShuttleRunRecord).toInt().toString()
        val fieldTrainingRecord = (random.nextFloat()*46+minFieldTrainingRecord).toInt().toString()

        val record = hashMapOf<String,String>(
            ChartManager.DATE to date,
            ChartManager.LEG_TUCK to regTuckRecord,
            ChartManager.SHUTTLE_RUN to shuttleRunRecord,
            ChartManager.FIELD_TRAINING to fieldTrainingRecord
        )

        userData.id?.let { id ->
            Firebase.firestore.collection(ChartManager.USERS)
                .document(id)
                .collection(ChartManager.TRAINING_RECORDS)
                .document(date)
                .set(record, SetOptions.merge())
        }
    }

    private fun calculateDate(date: String, dayDifference: Int): String{
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd").parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = Date(simpleDateFormat.time)
        calendar.add(Calendar.DAY_OF_MONTH, dayDifference)
        return SimpleDateFormat("yyyy.MM.dd").format(calendar.time)
    }
}
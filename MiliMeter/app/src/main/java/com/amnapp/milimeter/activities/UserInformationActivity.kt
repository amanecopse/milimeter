package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityUserInformationBinding
import java.text.SimpleDateFormat
import java.util.*

class UserInformationActivity :AppCompatActivity() {

    val binding by lazy { ActivityUserInformationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        // 창 닫기
        binding.cancelBt.setOnClickListener {
            val intentCancel = Intent(this, SettingActivity::class.java)
            startActivity(intentCancel)
            finish()
        }

        binding.backBt.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        val userData = UserData.getInstance()
        // 기존의 회원 정보 불러오기
        binding.nameEt.setText(userData.name)
        userData.birthDate?.let { birth -> binding.birthDateTv.setText(birth.toString()) }
        userData.height?.let { userHeight -> binding.heightEt.setText(userHeight.toString()) }
        userData.weight?.let { userWeight -> binding.weightEt.setText(userWeight.toString()) }
        userData.goalOfWeight?.let { goalOfWeight -> binding.goalOfWeightEt.setText(goalOfWeight.toString()) }
        userData.goalOfTotalGrade?.let { goalOfTotalRank -> binding.goalOfTotalRankSp.setSelection(goalOfTotalRank) }
        userData.goalOfLegTuckGrade?.let { goalOfLegTuckRank -> binding.goalOfLegTuckRankSp.setSelection(goalOfLegTuckRank) }
        userData.goalOfShuttleRunGrade?.let { goalOfShuttleRunRank -> binding.goalOfShuttleRunRankSp.setSelection(goalOfShuttleRunRank) }
        userData.goalOfFieldTrainingGrade?.let { goalOfFieldTrainingRank -> binding.goalOfFieldTrainingRankSp.setSelection(goalOfFieldTrainingRank) }

        binding.saveBt.setOnClickListener {

            // 새로운 회원의 정보 저장하기
            val newName = binding.nameEt.text
            val newBirth = binding.birthDateTv.text
            val newHeight = binding.heightEt.text
            val newWeight = binding.weightEt.text
            val newGoalWeight = binding.goalOfWeightEt.text
            val newGoalTotal = binding.goalOfTotalRankSp.selectedItemPosition
            val newGoalLegtuck = binding.goalOfLegTuckRankSp.selectedItemPosition
            val newGoalRunning = binding.goalOfShuttleRunRankSp.selectedItemPosition
            val newGoalTraining = binding.goalOfFieldTrainingRankSp.selectedItemPosition

            userData.name = newName.toString()
            userData.birthDate = if(newBirth == "생년월일 입력") null else newBirth.toString()
            userData.weight = if(newWeight.isNullOrEmpty()) null else newWeight.toString()
            userData.height = if(newHeight.isNullOrEmpty()) null else newHeight.toString()
            userData.goalOfWeight = if(newGoalWeight.isNullOrEmpty()) null else newGoalWeight.toString()
            userData.goalOfTotalGrade = newGoalTotal
            userData.goalOfLegTuckGrade = newGoalLegtuck
            userData.goalOfShuttleRunGrade = newGoalRunning
            userData.goalOfFieldTrainingGrade = newGoalTraining
            UserData.setInstance(userData)

            // 서버에 저장해야 함
            AccountManager().uploadUserData(userData){ message ->
                if(message == AccountManager.RESULT_SUCCESS)
                    showDialogMessage("수정 완료", "회원정보가 정상적으로 수정되었습니다")
            }
        }


        binding.birthDateLl.setOnClickListener {// 날짜선택 다이얼로그를 실행하고 선택한 날짜를 텍스트에 적용
            showDatePickerDialog()
        }
    }


    private fun showDatePickerDialog() {
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.birthDateTv.text = ""+year+"."+ String.format("%02d", month+1)+"."+String.format("%02d", dayOfMonth)
        }
        val year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val month = SimpleDateFormat("MM").format(Date()).toInt()
        val day = SimpleDateFormat("dd").format(Date()).toInt()

        if(UserData.getInstance().birthDate.isNullOrEmpty())
            DatePickerDialog(this, callBack,year-20,month,day).show()
        else{
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd").parse(UserData.getInstance().birthDate)
            val userYear = SimpleDateFormat("yyyy").format(simpleDateFormat).toInt()
            val userMonth = SimpleDateFormat("MM").format(simpleDateFormat).toInt()
            val userDay = SimpleDateFormat("dd").format(simpleDateFormat).toInt()
            DatePickerDialog(this, callBack,userYear,userMonth,userDay).show()
        }
    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> finish() }
        builder.show()
    }

}
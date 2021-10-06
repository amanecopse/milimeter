package com.amnapp.milimeter.activities

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityUserInformationBinding
import com.google.android.material.R
import java.text.SimpleDateFormat
import java.util.*

class UserInformationActivity :CustomThemeActivity() {

    lateinit var binding : ActivityUserInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userData = loadUserData()

        // 창 닫기
        binding.cancelIb.setOnClickListener {
            finish()
        }

        binding.backIb.setOnClickListener {
            finish()
        }

        binding.saveCv.setOnClickListener { // 새로운 회원의 정보 저장하기

            if(!checkEditTextError()){
                showDialogMessage("필수입력란 미기입", "비어있는 필수입력란을 기입해주세요"){}
                return@setOnClickListener
            }

            val newName = binding.nameEt.text
            val newMilitaryId = binding.militaryIdEt.text
            val newBirth = binding.birthDateTv.text
            val newHeight = binding.heightEt.text
            val newWeight = binding.weightEt.text
            val newGoalWeight = binding.goalOfWeightEt.text
            val newGoalTotal = binding.goalOfTotalGradeSp.selectedItemPosition
            val newGoalLegtuck = binding.goalOfLegTuckGradeSp.selectedItemPosition
            val newGoalRunning = binding.goalOfShuttleRunGradeSp.selectedItemPosition
            val newGoalTraining = binding.goalOfFieldTrainingGradeSp.selectedItemPosition

            userData.name = newName.toString()
            userData.militaryId = newMilitaryId.toString()
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
                    showDialogMessage("수정 완료", "회원정보가 정상적으로 수정되었습니다"){
                        finish()

                    }
            }
        }

        binding.nameEt.addTextChangedListener {// 입력값들의 오류체크
            checkEditTextError()
        }
        binding.militaryIdEt.addTextChangedListener {
            checkEditTextError()
        }
        binding.heightEt.addTextChangedListener {
            checkEditTextError()
        }
        binding.weightEt.addTextChangedListener {
            checkEditTextError()
        }


        binding.birthDateLl.setOnClickListener {// 날짜선택 다이얼로그를 실행하고 선택한 날짜를 텍스트에 적용
            showDatePickerDialog()
        }
    }

    private fun loadUserData(): UserData {
        val userData = UserData.getInstance()
        // 기존의 회원 정보 불러오기
        binding.nameEt.setText(userData.name)
        userData.birthDate?.let { birth -> binding.birthDateTv.setText(birth.toString()) }
        userData.militaryId?.let { militaryId -> binding.militaryIdEt.setText(militaryId.toString()) }
        userData.height?.let { userHeight -> binding.heightEt.setText(userHeight.toString()) }
        userData.weight?.let { userWeight -> binding.weightEt.setText(userWeight.toString()) }
        userData.goalOfWeight?.let { goalOfWeight -> binding.goalOfWeightEt.setText(goalOfWeight.toString()) }
        userData.goalOfTotalGrade?.let { goalOfTotalRank ->
            binding.goalOfTotalGradeSp.setSelection(
                goalOfTotalRank
            )
        }
        userData.goalOfLegTuckGrade?.let { goalOfLegTuckRank ->
            binding.goalOfLegTuckGradeSp.setSelection(
                goalOfLegTuckRank
            )
        }
        userData.goalOfShuttleRunGrade?.let { goalOfShuttleRunRank ->
            binding.goalOfShuttleRunGradeSp.setSelection(
                goalOfShuttleRunRank
            )
        }
        userData.goalOfFieldTrainingGrade?.let { goalOfFieldTrainingRank ->
            binding.goalOfFieldTrainingGradeSp.setSelection(
                goalOfFieldTrainingRank
            )
        }
        return userData
    }

    private fun checkEditTextError(): Boolean{

        var valid = true

        if (binding.nameEt.text.isNullOrEmpty()){
            binding.nameEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.nameTl.helperText = binding.nameEt.error
            valid = false
        }
        else{
            binding.nameEt.error = null
            binding.nameTl.helperText = null
        }

        if (binding.militaryIdEt.text.isNullOrEmpty()){
            binding.militaryIdEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.militaryIdTl.helperText = binding.militaryIdEt.error
            valid = false
        }
        else{
            binding.militaryIdEt.error = null
            binding.militaryIdTl.helperText = null
        }

        if (binding.heightEt.text.isNullOrEmpty()){
            binding.heightEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.heightTl.helperText = binding.heightEt.error
            valid = false
        }
        else{
            binding.heightEt.error = null
            binding.heightTl.helperText = null
        }

        if (binding.weightEt.text.isNullOrEmpty()){
            binding.weightEt.setError("필수입력란 입니다",
                ContextCompat.getDrawable(this, R.drawable.mtrl_ic_error))
            binding.weightTl.helperText = binding.weightEt.error
            valid = false
        }
        else{
            binding.weightEt.error = null
            binding.weightTl.helperText = null
        }

        return valid
    }


    private fun showDatePickerDialog() {
        val callBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.birthDateTv.text = ""+year+"."+ String.format("%02d", month+1)+"."+String.format("%02d", dayOfMonth)
        }
        val year = SimpleDateFormat("yyyy").format(Date()).toInt()
        val month = SimpleDateFormat("MM").format(Date()).toInt()
        val day = SimpleDateFormat("dd").format(Date()).toInt()

        if(UserData.getInstance().birthDate.isNullOrEmpty())
            DatePickerDialog(this, callBack,year-20,month+1,day).show()
        else{
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd").parse(UserData.getInstance().birthDate)
            val userYear = SimpleDateFormat("yyyy").format(simpleDateFormat).toInt()
            val userMonth = SimpleDateFormat("MM").format(simpleDateFormat).toInt()
            val userDay = SimpleDateFormat("dd").format(simpleDateFormat).toInt()
            DatePickerDialog(this, callBack,userYear,userMonth-1,userDay).show()
        }
    }

    fun showDialogMessage(title:String, body:String, callBack: ()-> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->  callBack}
        builder.show()
    }

}
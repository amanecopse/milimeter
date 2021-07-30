package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityUserInformationBinding

class UserInformationActivity :AppCompatActivity() {

    val binding by lazy { ActivityUserInformationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 창 닫기
        binding.cancelBt.setOnClickListener {
            val intentBack = Intent(this, SettingActivity::class.java)
            startActivity(intentBack)
        }

        binding.getBt.setOnClickListener {
            val userData = UserData.getInstance()
            // 기존의 회원 정보 불러오기
            binding.nameEt.setText(userData.name)
            userData.birthDate?.let { age -> binding.birthDateTv.setText(age.toString()) }
            userData.height?.let { userHeight -> binding.heightEt.setText(userHeight.toString()) }
            userData.weight?.let { userWeight -> binding.weightEt.setText(userWeight.toString()) }
            userData.goalOfWeight?.let { goalOfWeight -> binding.goalOfWeightEt.setText(goalOfWeight.toString()) }
            userData.goalOfTotalGrade?.let { goalOfTotalRank -> binding.goalOfTotalRankSp.setSelection(goalOfTotalRank) }
            userData.goalOfLegTuckGrade?.let { goalOfLegTuckRank -> binding.goalOfLegTuckRankSp.setSelection(goalOfLegTuckRank) }
            userData.goalOfShuttleRunGrade?.let { goalOfShuttleRunRank -> binding.goalOfShuttleRunRankSp.setSelection(goalOfShuttleRunRank) }
            userData.goalOfFieldTrainingGrade?.let { goalOfFieldTrainingRank -> binding.goalOfTotalRankSp.setSelection(goalOfFieldTrainingRank) }
        }

        binding.saveBt.setOnClickListener {
            //lateinit var onClickedListener: PwCheckDialog.ButtonClickListener
            val userData = UserData.getInstance()
            // 새로운 회원의 정보 저장하기
            val newName = binding.nameEt.text
            val newAge = binding.birthDateTv.text
            val newHeight = binding.heightEt.text
            val newWeight = binding.weightEt.text
            val newGoalWeight = binding.goalOfWeightEt.text
            val newGoalTotal = binding.goalOfTotalRankSp.selectedItemPosition
            val newGoalLegtuck = binding.goalOfLegTuckRankSp.selectedItemPosition
            val newGoalRunning = binding.goalOfShuttleRunRankSp.selectedItemPosition
            val newGoalTraining = binding.goalOfFieldTrainingRankSp.selectedItemPosition

            userData.name = newName.toString()
            userData.birthDate = if(newAge.isNullOrEmpty()) null else newAge.toString()
            userData.weight = if(newWeight.isNullOrEmpty()) null else newWeight.toString()
            userData.height = if(newHeight.isNullOrEmpty()) null else newHeight.toString()
            userData.goalOfWeight = if(newGoalWeight.isNullOrEmpty()) null else newGoalWeight.toString()
            userData.goalOfTotalGrade = newGoalTotal
            userData.goalOfLegTuckGrade = newGoalLegtuck
            userData.goalOfShuttleRunGrade = newGoalRunning
            userData.goalOfFieldTrainingGrade = newGoalTraining
            UserData.setInstance(userData)
        }
        // 서버에 저장해야 함
    }

}
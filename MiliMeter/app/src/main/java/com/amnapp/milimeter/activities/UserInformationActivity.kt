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
            binding.nameEt.setText(userData.userName)
            userData.userAge?.let { age -> binding.ageEt.setText(age.toString()) }
            userData.userHeight?.let { userHeight -> binding.heightEt.setText(userHeight.toString()) }
            userData.userWeight?.let { userWeight -> binding.weightEt.setText(userWeight.toString()) }
            userData.goalOfWeight?.let { goalOfWeight -> binding.goalOfWeightEt.setText(goalOfWeight.toString()) }
            userData.goalOfTotalRank?.let { goalOfTotalRank -> binding.goalOfTotalRankSp.setSelection(goalOfTotalRank) }
            userData.goalOfLegTuckRank?.let { goalOfLegTuckRank -> binding.goalOfLegTuckRankSp.setSelection(goalOfLegTuckRank) }
            userData.goalOfShuttleRunRank?.let { goalOfShuttleRunRank -> binding.goalOfShuttleRunRankSp.setSelection(goalOfShuttleRunRank) }
            userData.goalOfFieldTrainingRank?.let { goalOfFieldTrainingRank -> binding.goalOfTotalRankSp.setSelection(goalOfFieldTrainingRank) }
        }

        binding.saveBt.setOnClickListener {
            //lateinit var onClickedListener: PwCheckDialog.ButtonClickListener
            val userData = UserData.getInstance()
            // 새로운 회원의 정보 저장하기
            var newName = binding.nameEt.text
            var newAge = binding.ageEt.text
            var newHeight = binding.heightEt.text
            var newWeight = binding.weightEt.text
            var newGoalWeight = binding.goalOfWeightEt.text
            var newGoalTotal = binding.goalOfTotalRankSp.selectedItemPosition
            var newGoalLegtuck = binding.goalOfLegTuckRankSp.selectedItemPosition
            var newGoalRunning = binding.goalOfShuttleRunRankSp.selectedItemPosition
            var newGoalTraining = binding.goalOfFieldTrainingRankSp.selectedItemPosition

            userData.userName = newName.toString()
            userData.userAge = if(newAge.isNullOrEmpty()) null else newAge.toString().toInt()
            userData.userWeight = if(newWeight.isNullOrEmpty()) null else newWeight.toString().toInt()
            userData.userHeight = if(newHeight.isNullOrEmpty()) null else newHeight.toString().toInt()
            userData.goalOfWeight = if(newGoalWeight.isNullOrEmpty()) null else newGoalWeight.toString().toInt()
            userData.goalOfTotalRank = newGoalTotal
            userData.goalOfLegTuckRank = newGoalLegtuck
            userData.goalOfShuttleRunRank = newGoalRunning
            userData.goalOfFieldTrainingRank = newGoalTraining
            UserData.setInstance(userData)
        }
        // 서버에 저장해야 함
    }

}
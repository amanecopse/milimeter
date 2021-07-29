package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.UserData.Companion.getInstance
import com.amnapp.milimeter.databinding.ActivityUserInformationBinding
import com.google.common.primitives.UnsignedBytes.toInt

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
            // 기존의 회원 정보 불러오기
            binding.nameEt.setText("${getInstance().userName}")
            binding.ageEt.setText("${getInstance().userAge}")
            binding.heightEt.setText("${getInstance().userHeight}")
            binding.weightEt.setText("${getInstance().userWeight}")
            binding.goalOfWeightEt.setText("${getInstance().goalOfWeight}")
            getInstance().goalOfTotalRank?.let { it -> binding.goalOfTotalRankSp.setSelection(it) }
            getInstance().goalOfLegTuckRank?.let { it1 -> binding.goalOfLegTuckRankSp.setSelection(it1) }
            getInstance().goalOfShuttleRunRank?.let { it2 -> binding.goalOfShuttleRunRankSp.setSelection(it2) }
            getInstance().goalOfFieldTrainingRank?.let { it3 -> binding.goalOfTotalRankSp.setSelection(it3) }
        }

        binding.saveBt.setOnClickListener {
            lateinit var onClickedListener: PwCheckDialog.ButtonClickListener
            val userData = getInstance()
            // 새로운 회원의 정보 저장하기
            var newName = binding.nameEt.text
            var newAge = binding.ageEt.text
            var newHeight = binding.heightEt.text
            var newWeight = binding.weightEt.text
            var newGoalWeight = binding.goalOfWeightEt.text
            var newGoalTotal = binding.goalOfTotalRankSp.selectedItem
            var newGoalLegtuck = binding.goalOfLegTuckRankSp.selectedItem
            var newGoalRunning = binding.goalOfShuttleRunRankSp.selectedItem
            var newGoalTraining = binding.goalOfFieldTrainingRankSp.selectedItem

            userData.userName = newName.toString()
            userData.userAge = newAge.toString().toInt()
            userData.userWeight = newWeight.toString().toInt()
            userData.userHeight = newHeight.toString().toInt()
            userData.goalOfWeight = newGoalWeight.toString().toInt()
            userData.goalOfTotalRank = newGoalTotal.toString().toInt()
            userData.goalOfLegTuckRank = newGoalLegtuck.toString().toInt()
            userData.goalOfShuttleRunRank = newGoalRunning.toString().toInt()
            userData.goalOfFieldTrainingRank = newGoalTraining.toString().toInt()
            UserData.setInstance(userData)
        }
        // 서버에 저장해야 함
    }

}

private fun Unit.toInt() {
    TODO("Not yet implemented")
}

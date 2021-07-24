package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityEditSubUserInfoBinding

class EditSubUserInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditSubUserInfoBinding
    lateinit var mDialog: AlertDialog//로딩화면임. setProgressDialog()를 실행후 mDialog.show()로 시작
    lateinit var mSubUserData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSubUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        mSubUserData = UserData.mTmpUserData!!
        if (mSubUserData != null) {
            binding.nameTv.text = mSubUserData.userName
            binding.idTv.text = mSubUserData.id
            binding.pwEt.setText(mSubUserData.pw)
            mSubUserData.userAge?.let { binding.ageEt.setText(it.toString()) }
            mSubUserData.militaryId?.let { binding.militaryIdEt.setText(it.toString()) }
            mSubUserData.userHeight?.let { binding.heightEt.setText(it.toString()) }
            mSubUserData.userWeight?.let { binding.weightEt.setText(it.toString()) }
            mSubUserData.userBloodType?.let { binding.bloodTypeSp.setSelection(it) }
            mSubUserData.goalOfWeight?.let { binding.goalOfWeightEt.setText(it.toString()) }
            mSubUserData.goalOfTotalRank?.let { binding.goalOfTotalRankSp.setSelection(it) }
            mSubUserData.goalOfLegTuckRank?.let { binding.goalOfLegTuckRankSp.setSelection(it) }
            mSubUserData.goalOfShuttleRunRank?.let { binding.goalOfShuttleRunRankSp.setSelection(it) }
            mSubUserData.goalOfFieldTrainingRank?.let { binding.goalOfFieldTrainingRankSp.setSelection(it) }
        }

        binding.confirmCv.setOnClickListener{
            if(
                binding.nameTv.text.isNullOrEmpty()
                ||binding.pwEt.text.isNullOrEmpty()
                ||binding.militaryIdEt.text.isNullOrEmpty()
                ||binding.heightEt.text.isNullOrEmpty()
                ||binding.weightEt.text.isNullOrEmpty()
                ||binding.pwEt.text.isNullOrEmpty()
            ){
                showDialogMessage("입력 오류", "필수 입력란이 비어있습니다")
                return@setOnClickListener
            }

            if (mSubUserData != null) {
                mSubUserData.pw = binding.pwEt.text.toString()
                mSubUserData.userAge = if(binding.ageEt.text.isNullOrEmpty()) null else binding.ageEt.text.toString().toInt()
                mSubUserData.militaryId = binding.militaryIdEt.text.toString().toInt()
                mSubUserData.userHeight = binding.heightEt.text.toString().toInt()
                mSubUserData.userWeight = binding.weightEt.text.toString().toInt()
                mSubUserData.userBloodType = binding.bloodTypeSp.selectedItemPosition
                mSubUserData.goalOfWeight = if(binding.goalOfWeightEt.text.isNullOrEmpty()) null else binding.goalOfWeightEt.text.toString().toInt()
                mSubUserData.goalOfTotalRank = binding.goalOfTotalRankSp.selectedItemPosition
                mSubUserData.goalOfLegTuckRank = binding.goalOfLegTuckRankSp.selectedItemPosition
                mSubUserData.goalOfShuttleRunRank = binding.goalOfShuttleRunRankSp.selectedItemPosition
                mSubUserData.goalOfFieldTrainingRank = binding.goalOfFieldTrainingRankSp.selectedItemPosition
            }

        }

    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }
}
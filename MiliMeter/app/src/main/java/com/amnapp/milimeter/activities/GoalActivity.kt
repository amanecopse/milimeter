package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.ChartManager
import com.amnapp.milimeter.R
import com.amnapp.milimeter.UserData
import com.amnapp.milimeter.databinding.ActivityGoalBinding
import com.amnapp.milimeter.databinding.ActivityHomeBinding
import kotlin.system.exitProcess

class GoalActivity : CustomThemeActivity() {

    lateinit var binding: ActivityGoalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()

        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        // 각 아이콘 창으로 이동 -> 아이콘 버튼 클릭시 화면 전환
        binding.homeBt.setOnClickListener {
            val homeintent = Intent(this, HomeActivity::class.java)
            startActivity(homeintent)
        }

        binding.bodyBt.setOnClickListener {
            val bodyintent = Intent(this, BodyActivity::class.java)
            startActivity(bodyintent)
        }

        binding.resultBt.setOnClickListener {
            val resultintent = Intent(this, ResultActivity::class.java)
            startActivity(resultintent)
        }

        binding.goalBt.setOnClickListener {
            val goalintent = Intent(this, GoalActivity::class.java)
            startActivity(goalintent)
        }

        binding.settingBt.setOnClickListener {
            val settingintent = Intent(this, SettingActivity::class.java)
            startActivity(settingintent)
        }

        showCircleProgressBar()
    }

    private fun showCircleProgressBar() {
        val cm = ChartManager()
        val userData = UserData.getInstance()

        cm.loadTrainingRecordNDaysAgo(userData, cm.getCurrentDateBasedOnFormat(), 8){docs, lineDataSets, dateList ->
            val myLegTuckScore = cm.findMaxInDocs(docs, ChartManager.LEG_TUCK)//데이터가 없으면 -1 반환
            val myShuttleRunScore = cm.findMinInDocs(docs, ChartManager.SHUTTLE_RUN)
            val myFieldTrainingScore = cm.findMinInDocs(docs, ChartManager.FIELD_TRAINING)

            val myLegTuckGrade = cm.calculateGradeIndex(myLegTuckScore, ChartManager.LEG_TUCK)
            val myShuttleRunGrade = cm.calculateGradeIndex(myShuttleRunScore, ChartManager.SHUTTLE_RUN)
            val myFieldTrainingGrade = cm.calculateGradeIndex(myFieldTrainingScore, ChartManager.FIELD_TRAINING)

            val goalOfLegTuckGrade = userData.goalOfLegTuckGrade
            val goalOfShuttleRunGrade = userData.goalOfShuttleRunGrade
            val goalOfFieldTrainingGrade = userData.goalOfFieldTrainingGrade

            val goalOfLegTuckScore = cm.calculateBoundaryScore(goalOfLegTuckGrade.toString().toInt(), ChartManager.LEG_TUCK)//9급이거나 잘못된 값을 넣으면 -1반환
            val goalOfShuttleRunScore = cm.calculateBoundaryScore(goalOfShuttleRunGrade.toString().toInt(), ChartManager.SHUTTLE_RUN)
            val goalOfFieldTrainingScore = cm.calculateBoundaryScore(goalOfFieldTrainingGrade.toString().toInt(), ChartManager.FIELD_TRAINING)

            if(goalOfLegTuckScore != -1 && myLegTuckScore != -1){
                cm.makeCircleProgressBar(binding.legTuckCpb, myLegTuckScore, goalOfLegTuckScore)
                binding.goalOfLegTuckGradeTv.text = cm.convertIndexToGrade(goalOfLegTuckGrade!!)
                    binding.legTuckGradeTv.text = cm.convertIndexToGrade(myLegTuckGrade.toInt())
            }
            else if(goalOfLegTuckScore != -1){
                binding.goalOfLegTuckGradeTv.text = goalOfLegTuckGrade?.let {
                    cm.convertIndexToGrade(
                        it
                    )
                }
            }
            else if(myLegTuckScore != -1){
                binding.legTuckGradeTv.text = myLegTuckGrade.let {
                    cm.convertIndexToGrade(
                        it.toInt()
                    )
                }
            }

            if(goalOfShuttleRunScore != -1 && myShuttleRunScore != -1){
                cm.makeCircleProgressBar(binding.shuttleRunCpb, 10 - myShuttleRunGrade.toInt(), 10 - goalOfShuttleRunGrade!!)
                binding.goalOfShuttleRunGradeTv.text = cm.convertIndexToGrade(goalOfShuttleRunGrade!!)
                binding.shuttleRunGradeTv.text = cm.convertIndexToGrade(myShuttleRunGrade.toInt())
            }
            else if(goalOfLegTuckScore != -1){
                binding.goalOfShuttleRunGradeTv.text = goalOfShuttleRunGrade?.let {
                    cm.convertIndexToGrade(
                        it
                    )
                }
            }
            else if(myShuttleRunScore != -1){
                binding.shuttleRunGradeTv.text = myShuttleRunGrade.let {
                    cm.convertIndexToGrade(
                        it.toInt()
                    )
                }
            }

            if(goalOfFieldTrainingScore != -1 && myFieldTrainingScore != -1){
                cm.makeCircleProgressBar(binding.fieldTrainingCpb, 10 - myFieldTrainingGrade.toInt(), 10 - goalOfFieldTrainingGrade!!)
                binding.goalOfFieldTrainingGradeTv.text = cm.convertIndexToGrade(goalOfFieldTrainingGrade!!)
                binding.fieldTrainingGradeTv.text = cm.convertIndexToGrade(myFieldTrainingGrade.toInt())
            }
            else if(goalOfFieldTrainingScore != -1){
                binding.goalOfFieldTrainingGradeTv.text = goalOfFieldTrainingGrade?.let {
                    cm.convertIndexToGrade(
                        it
                    )
                }
            }
            else if(myFieldTrainingScore != -1){
                binding.fieldTrainingGradeTv.text = myFieldTrainingGrade.let {
                    cm.convertIndexToGrade(
                        it.toInt()
                    )
                }
            }
        }
    }

    fun showTwoButtonDialogMessage(title: String, body: String, callBack: (Int) -> Unit) {//다이얼로그 메시지를 띄우는 함수
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int -> callBack(i)}
        builder.show()
    }

    override fun onBackPressed() {
        showTwoButtonDialogMessage("알림", "Mili Meter를 종료하시겠습니까?"){
            when(it){
                -1 -> {
                    finishAffinity()
                    exitProcess(0)
                }
            }
        }
    }
}
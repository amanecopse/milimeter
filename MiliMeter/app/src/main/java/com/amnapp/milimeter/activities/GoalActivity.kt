package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R
import com.amnapp.milimeter.databinding.ActivityGoalBinding
import com.amnapp.milimeter.databinding.ActivityHomeBinding

class GoalActivity : AppCompatActivity() {

    val binding by lazy { ActivityGoalBinding.inflate(layoutInflater) }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(R.layout.activity_goal)
        setContentView(binding.root)

        // 각 아이콘 창으로 이동 -> 아이콘 버튼 클릭시 화면 전환
        binding.homeBt.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        // 담당 미정 - body창(합친 뒤에 주석 없애주세요)
//        binding.bodyBt.setOnClickListener {
//            val intent = Intent(this, BodyActivity::class.java)
//            startActivity(intent)
//        }
        // 고동현님 프로젝트 담당 부분 - result창(합친 뒤에 주석 없애주세요)
//        binding.resultBt.setOnClickListener {
//            val intent = Intent(this, ResultActivity::class.java)
//            startActivity(intent)
//        }

        binding.goalBt.setOnClickListener {
            val intent = Intent(this, GoalActivity::class.java)
            startActivity(intent)
        }

        binding.settingBt.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}
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
    }
}
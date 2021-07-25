package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R

class LegtuckResultActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leg_result)

        //버튼
        val CurrentButton =findViewById<Button>(R.id.all)
        CurrentButton.setOnClickListener{
            val currentintent = Intent(this, ResultActivity::class.java)
            startActivity(currentintent)
        }
        //달리기
        val runButton =findViewById<Button>(R.id.run)
        runButton.setOnClickListener{
            val runintent =Intent(this,RunningResultActivity::class.java)
            startActivity(runintent)
        }
        //전장순환
        val circuitButton=findViewById<Button>(R.id.circuit)
        circuitButton.setOnClickListener{
            val circuitintent =Intent(this,CircuitResultActivity::class.java)
            startActivity(circuitintent)
        }


        //그래프

        //등급표시
    }

    //바그래프 저장

}

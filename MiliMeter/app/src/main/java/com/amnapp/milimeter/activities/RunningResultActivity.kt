package com.amnapp.milimeter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.R

class RunningCurrentActivity: AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.`activity_running_result`)

            //버튼
            val CurrentButton =findViewById<Button>(R.id.all)
            CurrentButton.setOnClickListener{
                val currentintent = Intent(this, ResultActivity::class.java)
                startActivity(currentintent)
            }
            //레그턱 버튼
            val legButton =findViewById<Button>(R.id.legtuckbutton)
            legButton.setOnClickListener{
                val legintent =Intent(this, LegtuckCurrentActivity::class.java)
                startActivity(legintent)
            }
            //전장순환
            val circuitButton=findViewById<Button>(R.id.circuit)
            circuitButton.setOnClickListener{
                val circuitintent =Intent(this,CircuitCurrentActivity::class.java)
                startActivity(circuitintent)
            }

        }

    //그래프

    //등급표시
}

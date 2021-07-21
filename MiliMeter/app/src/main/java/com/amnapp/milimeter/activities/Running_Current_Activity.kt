package com.amnapp.milimeter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Running_Current_Activity: AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_running_current)

            //버튼
            var CurrentButton =findViewById<Button>(R.id.all)
            CurrentButton.setOnClickListener{
                val currentintent = Intent(this, Current_Activity::class.java)
                startActivity(currentintent)
            }
            //레그턱 버튼
            var legButton =findViewById<Button>(R.id.legtuckbutton)
            legButton.setOnClickListener{
                val legintent =Intent(this, Legtuck_Current_Activity::class.java)
                startActivity(legintent)
            }
            //전장순환
            var circuitButton=findViewById<Button>(R.id.circuit)
            circuitButton.setOnClickListener{
                val circuitintent =Intent(this,Circuit_Current_Activity::class.java)
                startActivity(circuitintent)
            }

        }

    //그래프

    //등급표시
}

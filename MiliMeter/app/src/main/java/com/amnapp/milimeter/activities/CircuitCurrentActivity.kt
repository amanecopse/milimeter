package com.amnapp.milimeter.activities
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CircuitCurrentActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circuit_current)

        //버튼
        val CurrentButton = findViewById<Button>(R.id.all)
        CurrentButton.setOnClickListener {
            val currentintent = Intent(this, Current_Activity::class.java)
            startActivity(currentintent)
        }
        //레그턱
        val legButton = findViewById<Button>(R.id.legtuckbutton)
        legButton.setOnClickListener {
            val legintent = Intent(this, Legtuck_Current_Activity::class.java)
            startActivity(legintent)
        }
        //240m가는 버튼
        val runButton = findViewById<Button>(R.id.run)
        runButton.setOnClickListener {
            val runintent = Intent(this, Running_Current_Activity::class.java)
            startActivity(runintent)

        }
    }
}

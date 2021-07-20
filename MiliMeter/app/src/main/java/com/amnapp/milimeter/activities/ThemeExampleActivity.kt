package com.amnapp.milimeter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R

class ThemeExampleActivity : AppCompatActivity() {
    var mTheme: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme()
        setContentView(R.layout.activity_theme_example)

        findViewById<Button>(R.id.changeThemeBt).setOnClickListener {
            val pm = PreferenceManager()
            if(mTheme == PreferenceManager.THEME_DARK){
                pm.setThemeData(this, PreferenceManager.THEME_DEFAULT)
            }
            else{
                pm.setThemeData(this, PreferenceManager.THEME_DARK)
            }
            Toast.makeText(this, pm.getThemeData(this), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ThemeExampleActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loadTheme() {
        val pm = PreferenceManager()
        mTheme = pm.getThemeData(this)
        if(mTheme == PreferenceManager.THEME_DARK){
            setTheme(R.style.DarkAppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
    }
}
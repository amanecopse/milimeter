package com.amnapp.milimeter.activities

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R

open class CustomThemeActivity : AppCompatActivity() {

    var mTheme: String? = null

    fun loadTheme() {
        val pm = PreferenceManager()
        mTheme = pm.getThemeData(this)
        if(mTheme == PreferenceManager.THEME_DARK){
            setTheme(R.style.DarkAppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
        Log.d("loadTheme","검사")

    }

}
package com.amnapp.milimeter.activities

import android.content.DialogInterface
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amnapp.milimeter.PreferenceManager
import com.amnapp.milimeter.R

open class CustomThemeActivity : AppCompatActivity() {

    var mTheme: String? = null
    var mode = "basic"
    val pm = PreferenceManager()

    fun loadTheme() {
        val pm = PreferenceManager()
        mTheme = pm.getThemeData(this)
        if(mTheme == PreferenceManager.THEME_DARK){
            setTheme(R.style.DarkAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_CREAM) {
            setTheme(R.style.CreamAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_VANILAYELLOW) {
            setTheme(R.style.VanilaYellowAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTYELLOW) {
            setTheme(R.style.LightYellowAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_MUSTARDYELLOW) {
            setTheme(R.style.MustardYellowAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTPINKORANGE) {
            setTheme(R.style.LightPinkOrangeAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_ORANGE) {
            setTheme(R.style.OrangeAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_DARKGOLDENYELLOW) {
            setTheme(R.style.DarkGoldenYellowAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTBROWN) {
            setTheme(R.style.LightBrownAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_PINK) {
            setTheme(R.style.PinkAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_RED) {
            setTheme(R.style.RedAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_REDWINE) {
            setTheme(R.style.RedWineAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_PURPLE) {
            setTheme(R.style.PurpleAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTGREEN) {
            setTheme(R.style.LightGreenAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTLIME) {
            setTheme(R.style.LightLimeAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_MINT) {
            setTheme(R.style.MintAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_GRAYGREEN) {
            setTheme(R.style.GrayGreenAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTBLUEGREEN) {
            setTheme(R.style.LightBlueGreenAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTBLUE) {
            setTheme(R.style.LightBlueAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_COBALT) {
            setTheme(R.style.CobaltAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_DARKBLACKBLUE) {
            setTheme(R.style.DarkBlackBlueAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_BEIGE) {
            setTheme(R.style.BeigeAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_BEIGEBROWN) {
            setTheme(R.style.BeigeBrownAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_LIGHTGRAY) {
            setTheme(R.style.LightGrayAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_GRAY) {
            setTheme(R.style.GrayAppTheme)
        }
        else if (mTheme == PreferenceManager.THEME_SPECIAL_FIRST) {
            setTheme(R.style.SpecialAppTheme1)
        }
        else if (mTheme == PreferenceManager.THEME_SPECIAL_SECOND) {
            setTheme(R.style.SpecialAppTheme2)
        }
        else if (mTheme == PreferenceManager.THEME_SPECIAL_THIRD) {
            setTheme(R.style.SpecialAppTheme3)
        }
        else{
            setTheme(R.style.AppTheme)
        }

    }


    fun colorBtInactivation() {
        findViewById<Button>(R.id.creamBt).setEnabled(false)
        findViewById<Button>(R.id.vanilaYellowBt).setEnabled(false)
        findViewById<Button>(R.id.lightYellowBt).setEnabled(false)
        findViewById<Button>(R.id.mustardYellowBt).setEnabled(false)
        findViewById<Button>(R.id.lightPinkOrangeBt).setEnabled(false)
        findViewById<Button>(R.id.orangeBt).setEnabled(false)
        findViewById<Button>(R.id.darkGoldenYellowBt).setEnabled(false)
        findViewById<Button>(R.id.lightBrownBt).setEnabled(false)
        findViewById<Button>(R.id.pinkBt).setEnabled(false)
        findViewById<Button>(R.id.redBt).setEnabled(false)
        findViewById<Button>(R.id.redWineBt).setEnabled(false)
        findViewById<Button>(R.id.purpleBt).setEnabled(false)
        findViewById<Button>(R.id.lightGreenBt).setEnabled(false)
        findViewById<Button>(R.id.lightLimeBt).setEnabled(false)
        findViewById<Button>(R.id.mintBt).setEnabled(false)
        findViewById<Button>(R.id.grayGreenBt).setEnabled(false)
        findViewById<Button>(R.id.lightBlueGreenBt).setEnabled(false)
        findViewById<Button>(R.id.lightBlueBt).setEnabled(false)
        findViewById<Button>(R.id.cobaltBt).setEnabled(false)
        findViewById<Button>(R.id.darkBlackBlueBt).setEnabled(false)
        findViewById<Button>(R.id.beigeBt).setEnabled(false)
        findViewById<Button>(R.id.beigeBrownBt).setEnabled(false)
        findViewById<Button>(R.id.lightGrayBt).setEnabled(false)
        findViewById<Button>(R.id.grayBt).setEnabled(false)
    }

    fun colorBtActivation() {
        findViewById<Button>(R.id.creamBt).setEnabled(true)
        findViewById<Button>(R.id.vanilaYellowBt).setEnabled(true)
        findViewById<Button>(R.id.lightYellowBt).setEnabled(true)
        findViewById<Button>(R.id.mustardYellowBt).setEnabled(true)
        findViewById<Button>(R.id.lightPinkOrangeBt).setEnabled(true)
        findViewById<Button>(R.id.orangeBt).setEnabled(true)
        findViewById<Button>(R.id.darkGoldenYellowBt).setEnabled(true)
        findViewById<Button>(R.id.lightBrownBt).setEnabled(true)
        findViewById<Button>(R.id.pinkBt).setEnabled(true)
        findViewById<Button>(R.id.redBt).setEnabled(true)
        findViewById<Button>(R.id.redWineBt).setEnabled(true)
        findViewById<Button>(R.id.purpleBt).setEnabled(true)
        findViewById<Button>(R.id.lightGreenBt).setEnabled(true)
        findViewById<Button>(R.id.lightLimeBt).setEnabled(true)
        findViewById<Button>(R.id.mintBt).setEnabled(true)
        findViewById<Button>(R.id.grayGreenBt).setEnabled(true)
        findViewById<Button>(R.id.lightBlueGreenBt).setEnabled(true)
        findViewById<Button>(R.id.lightBlueBt).setEnabled(true)
        findViewById<Button>(R.id.cobaltBt).setEnabled(true)
        findViewById<Button>(R.id.darkBlackBlueBt).setEnabled(true)
        findViewById<Button>(R.id.beigeBt).setEnabled(true)
        findViewById<Button>(R.id.beigeBrownBt).setEnabled(true)
        findViewById<Button>(R.id.lightGrayBt).setEnabled(true)
        findViewById<Button>(R.id.grayBt).setEnabled(true)
    }

    fun clickChangeText(button: Button) {
        button.setOnClickListener {
            reset()
            button.setText("선택")
        }
    }

    fun reset() {
        findViewById<Button>(R.id.creamBt).setText("")
        findViewById<Button>(R.id.vanilaYellowBt).setText("")
        findViewById<Button>(R.id.lightYellowBt).setText("")
        findViewById<Button>(R.id.mustardYellowBt).setText("")
        findViewById<Button>(R.id.lightPinkOrangeBt).setText("")
        findViewById<Button>(R.id.orangeBt).setText("")
        findViewById<Button>(R.id.darkGoldenYellowBt).setText("")
        findViewById<Button>(R.id.lightBrownBt).setText("")
        findViewById<Button>(R.id.pinkBt).setText("")
        findViewById<Button>(R.id.redBt).setText("")
        findViewById<Button>(R.id.redWineBt).setText("")
        findViewById<Button>(R.id.purpleBt).setText("")
        findViewById<Button>(R.id.lightGreenBt).setText("")
        findViewById<Button>(R.id.lightLimeBt).setText("")
        findViewById<Button>(R.id.mintBt).setText("")
        findViewById<Button>(R.id.grayGreenBt).setText("")
        findViewById<Button>(R.id.lightBlueGreenBt).setText("")
        findViewById<Button>(R.id.lightBlueBt).setText("")
        findViewById<Button>(R.id.cobaltBt).setText("")
        findViewById<Button>(R.id.darkBlackBlueBt).setText("")
        findViewById<Button>(R.id.beigeBt).setText("")
        findViewById<Button>(R.id.beigeBrownBt).setText("")
        findViewById<Button>(R.id.lightGrayBt).setText("")
        findViewById<Button>(R.id.grayBt).setText("")
    }

    fun allColorBtClickChangeText() {
        clickChangeText(findViewById(R.id.creamBt))
        clickChangeText(findViewById(R.id.vanilaYellowBt))
        clickChangeText(findViewById(R.id.lightYellowBt))
        clickChangeText(findViewById(R.id.mustardYellowBt))
        clickChangeText(findViewById(R.id.lightPinkOrangeBt))
        clickChangeText(findViewById(R.id.orangeBt))
        clickChangeText(findViewById(R.id.darkGoldenYellowBt))
        clickChangeText(findViewById(R.id.lightBrownBt))
        clickChangeText(findViewById(R.id.pinkBt))
        clickChangeText(findViewById(R.id.redBt))
        clickChangeText(findViewById(R.id.redWineBt))
        clickChangeText(findViewById(R.id.purpleBt))
        clickChangeText(findViewById(R.id.lightGreenBt))
        clickChangeText(findViewById(R.id.lightLimeBt))
        clickChangeText(findViewById(R.id.mintBt))
        clickChangeText(findViewById(R.id.grayGreenBt))
        clickChangeText(findViewById(R.id.lightBlueGreenBt))
        clickChangeText(findViewById(R.id.lightBlueBt))
        clickChangeText(findViewById(R.id.cobaltBt))
        clickChangeText(findViewById(R.id.darkBlackBlueBt))
        clickChangeText(findViewById(R.id.beigeBt))
        clickChangeText(findViewById(R.id.beigeBrownBt))
        clickChangeText(findViewById(R.id.lightGrayBt))
        clickChangeText(findViewById(R.id.grayBt))
    }

    fun findColor() {
        if (findViewById<Button>(R.id.creamBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_CREAM)
        }
        else if (findViewById<Button>(R.id.vanilaYellowBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_VANILAYELLOW)
        }
        else if (findViewById<Button>(R.id.lightYellowBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTYELLOW)
        }
        else if (findViewById<Button>(R.id.mustardYellowBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_MUSTARDYELLOW)
        }
        else if (findViewById<Button>(R.id.lightPinkOrangeBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTPINKORANGE)
        }
        else if (findViewById<Button>(R.id.orangeBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_ORANGE)
        }
        else if (findViewById<Button>(R.id.darkGoldenYellowBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_DARKGOLDENYELLOW)
        }
        else if (findViewById<Button>(R.id.lightBrownBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTBROWN)
        }
        else if (findViewById<Button>(R.id.pinkBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_PINK)
        }
        else if (findViewById<Button>(R.id.redBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_RED)
        }
        else if (findViewById<Button>(R.id.redWineBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_REDWINE)
        }
        else if (findViewById<Button>(R.id.purpleBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_PURPLE)
        }
        else if (findViewById<Button>(R.id.lightGreenBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTGREEN)
        }
        else if (findViewById<Button>(R.id.lightLimeBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTLIME)
        }
        else if (findViewById<Button>(R.id.mintBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_MINT)
        }
        else if (findViewById<Button>(R.id.grayGreenBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_GRAYGREEN)
        }
        else if (findViewById<Button>(R.id.lightBlueGreenBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTBLUEGREEN)
        }
        else if (findViewById<Button>(R.id.lightBlueBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTBLUE)
        }
        else if (findViewById<Button>(R.id.cobaltBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_COBALT)
        }
        else if (findViewById<Button>(R.id.darkBlackBlueBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_DARKBLACKBLUE)
        }
        else if (findViewById<Button>(R.id.beigeBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_BEIGE)
        }
        else if (findViewById<Button>(R.id.beigeBrownBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_BEIGEBROWN)
        }
        else if (findViewById<Button>(R.id.lightGrayBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_LIGHTGRAY)
        }
        else if (findViewById<Button>(R.id.grayBt).text == "선택") {
            pm.setThemeData(this, PreferenceManager.THEME_GRAY)
        }
        else {
            pm.setThemeData(this, PreferenceManager.THEME_DEFAULT)
        }
    }

    fun showDialogMessage(title:String, body:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(body)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
    }

}
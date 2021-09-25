package com.amnapp.milimeter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


class PreferenceManager {
    private val PREFERENCE_NAME_LOGIN = "login preference"
    private val PREFERENCE_NAME_SETTINGS = "settings preference"
    private val LOGIN_DATA_ID = "id"
    private val LOGIN_DATA_PW = "pw"
    private val LOGIN_DATA_GC = "gc"
    private val LOGIN_DATA_AUTO_LOGIN_ENABLE = "auto login"
    private val SETTINGS_DATA_THEME = "theme"
    private val SETTINGS_DATA_LANGUAGE = "language"
    private val SETTINGS_DATA_PROFILE_IMAGE = "profile image"
    private val SETTINGS_DATA_DDAY = "D-Day"

    companion object{
        val THEME_DEFAULT = "default theme"
        val THEME_DARK = "dark theme"
        val THEME_CREAM = "cream theme"
        val THEME_VANILAYELLOW = "vanilayellow theme"
        val THEME_LIGHTYELLOW = "lightyellow theme"
        val THEME_MUSTARDYELLOW = "mustardyellow theme"
        val THEME_LIGHTPINKORANGE = "lightpinkorange theme"
        val THEME_ORANGE = "orange theme"
        val THEME_DARKGOLDENYELLOW = "darkgoldenyellow theme"
        val THEME_LIGHTBROWN = "lightbrown theme"
        val THEME_PINK = "pink theme"
        val THEME_RED = "red theme"
        val THEME_REDWINE = "redwine theme"
        val THEME_PURPLE = "purple theme"
        val THEME_LIGHTGREEN = "lightgreen theme"
        val THEME_LIGHTLIME = "lightlime theme"
        val THEME_MINT = "mint theme"
        val THEME_GRAYGREEN = "graygreen theme"
        val THEME_LIGHTBLUEGREEN = "lightbluegreen theme"
        val THEME_LIGHTBLUE = "lightblue theme"
        val THEME_COBALT = "cobalt theme"
        val THEME_DARKBLACKBLUE = "darkblackblue theme"
        val THEME_BEIGE = "beige theme"
        val THEME_BEIGEBROWN = "beigebrown theme"
        val THEME_LIGHTGRAY = "lightgray theme"
        val THEME_GRAY = "gray theme"

    }


    private fun getLoginPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE)
    }

    private fun getSettingsPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(PREFERENCE_NAME_SETTINGS, Context.MODE_PRIVATE)
    }

    fun setLoginData(context: Context, id:String, pw:String, autoLoginEnable: Boolean){
        val prefs = getLoginPreference(context)
        val editor = prefs?.edit()
        editor?.putString(LOGIN_DATA_ID,id)
        editor?.putString(LOGIN_DATA_PW,pw)
        editor?.putBoolean(LOGIN_DATA_AUTO_LOGIN_ENABLE, autoLoginEnable)
        editor?.apply()
    }

    fun setGroupCode(context: Context, groupCode: String){
        val prefs = getLoginPreference(context)
        val editor = prefs?.edit()
        editor?.putString(LOGIN_DATA_GC,groupCode)
        editor?.apply()
    }

    fun setThemeData(context: Context, theme: String){
        val prefs = getSettingsPreference(context)
        val editor = prefs?.edit()
        editor?.putString(SETTINGS_DATA_THEME,theme)
        editor?.apply()
    }

    fun setLanguageData(context: Context, language: String) {
        val prefs = getSettingsPreference(context)
        val editor = prefs?.edit()
        editor?.putString(SETTINGS_DATA_LANGUAGE,language)
        editor?.apply()
    }

    fun setProfileImage(context: Context, bitmap: Bitmap) {
        val prefs = getSettingsPreference(context)
        val editor = prefs?.edit()

        val baos1 = ByteArrayOutputStream()// bitmap to string
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos1)
        val bytes1: ByteArray = baos1.toByteArray()
        val bitmapString: String = Base64.encodeToString(bytes1, Base64.DEFAULT)

        editor?.putString(SETTINGS_DATA_PROFILE_IMAGE,bitmapString)
        editor?.apply()
    }

    fun setDDay(context: Context, dDay: Int) {
        val prefs = getSettingsPreference(context)
        val editor = prefs?.edit()

        editor?.putInt(SETTINGS_DATA_DDAY,dDay)
        editor?.apply()
    }

    fun getLoginData(context: Context): Array<String?> {
        val prefs = getLoginPreference(context)
        val id = prefs?.getString(LOGIN_DATA_ID, "")
        val pw = prefs?.getString(LOGIN_DATA_PW, "")
        return arrayOf(id, pw)
    }

    fun isAutoLoginEnable(context: Context): Boolean {
        val prefs = getLoginPreference(context)
        if(prefs == null)
            return false
        else
            return prefs.getBoolean(LOGIN_DATA_AUTO_LOGIN_ENABLE, false)
    }

    fun getGroupCode(context: Context): String?{
        val prefs = getLoginPreference(context)
        val groupCode = prefs?.getString(LOGIN_DATA_GC, "")
        return groupCode
    }

    fun getThemeData(context: Context): String? {
        val prefs = getSettingsPreference(context)
        val theme = prefs?.getString(SETTINGS_DATA_THEME, "")
        return theme
    }

    fun getLanguageData(context: Context): String? {
        val prefs = getSettingsPreference(context)
        val language = prefs?.getString(SETTINGS_DATA_LANGUAGE,"")
        return language
    }

    fun getProfileImage(context: Context): Bitmap? {
        val prefs = getSettingsPreference(context)
        val bitmapString = prefs?.getString(SETTINGS_DATA_PROFILE_IMAGE,null)

        var bitmap: Bitmap? = null
        if(bitmapString != null){
            val encodeByte1: ByteArray = Base64.decode(bitmapString, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.size)
        }

        return bitmap
    }

    fun getDDay(context: Context): Int? {
        val prefs = getSettingsPreference(context)
        var dDay: Int? = prefs?.getInt(SETTINGS_DATA_DDAY,-77777)// -77777은 null값을 기본값으로 할 수 없어서 어쩔 수 없이 넣는 값
        dDay = if(dDay == -77777) null else dDay

        return dDay
    }
}
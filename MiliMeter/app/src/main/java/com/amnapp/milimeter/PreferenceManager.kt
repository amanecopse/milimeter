package com.amnapp.milimeter

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    private val PREFERENCE_NAME_LOGIN = "login preference"
    private val PREFERENCE_NAME_SETTINGS = "settings preference"
    private val LOGIN_DATA_ID = "id"
    private val LOGIN_DATA_PW = "pw"
    private val LOGIN_DATA_GC = "gc"
    private val LOGIN_DATA_AUTO_LOGIN_ENABLE = "auto login"
    private val SETTINGS_DATA_THEME = "theme"

    companion object{
        val THEME_DARK = "dark theme"
        val THEME_DEFAULT = "default theme"
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

}
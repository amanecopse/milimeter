package com.amnapp.milimeter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferenceManager {
    private val PREFERENCE_NAME_LOGIN = "login preference"
    private val PREFERENCE_NAME_SETTINGS = "settings preference"
    private val LOGIN_DATA_ID = "id"
    private val LOGIN_DATA_PW = "pw"
    private val LOGIN_DATA_GC = "gc"
    private val LOGIN_DATA_AUTO_LOGIN_ENABLE = "auto login"
    private val SETTINGS_DATA_THEME = "theme"
    private val SETTINGS_DATA_LANGUAGE = "language"

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
}
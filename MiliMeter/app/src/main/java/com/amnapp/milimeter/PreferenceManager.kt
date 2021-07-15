package com.amnapp.milimeter

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    private val PREFERENCE_NAME_LOGIN = "login preference"
    private val PREFERENCE_NAME_SETTINGS = "settings preference"
    private val LOGIN_DATA_ID = "id"
    private val LOGIN_DATA_PW = "pw"
    private val LOGIN_DATA_GC = "gc"


    private fun getLoginPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE)
    }

    fun setLoginData(context: Context, id:String, pw:String, groupCode:String){
        val prefs = getLoginPreference(context)
        val editor = prefs?.edit()
        editor?.putString(LOGIN_DATA_ID,id)
        editor?.putString(LOGIN_DATA_PW,pw)
        editor?.putString(LOGIN_DATA_GC,groupCode)
        editor?.apply()
    }

    fun getLoginData(context: Context): Array<String?> {
        val prefs = getLoginPreference(context)
        val id = prefs?.getString(LOGIN_DATA_ID, "")
        val pw = prefs?.getString(LOGIN_DATA_PW, "")
        val groupCode = prefs?.getString(LOGIN_DATA_GC, "")
        return arrayOf(id, pw, groupCode)
    }

}
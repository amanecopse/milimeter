package com.amnapp.milimeter

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

object LocaleWrapper {
    private var sLocale: Locale? = null
    fun wrap(base: Context): Context {
        if (sLocale == null) {
            return base
        }
        val res: Resources = base.resources
        val config: Configuration = res.configuration
        config.setLocale(sLocale)
        return base.createConfigurationContext(config)
    }

    fun setLocale(lang: String?) {
        sLocale = Locale(lang)
    }
}
package com.mtc.general

import android.content.Context
import android.util.Log
import java.util.*

object LanguageUtil {
    fun setAppLanguage(context: Context, language: String): Context {
        return LanguageUtil.applyLanguage(context, language)
    }

    fun getDefaultLanguage(): String {
        var locale = Locale.getDefault()
        locale = when {
            locale.country.equals(Locales.Simplified_Chinese.country, true) ->
                Locale.SIMPLIFIED_CHINESE
            locale.country.equals(Locales.Traditional_Chinese.country, true) ->
                Locale.TRADITIONAL_CHINESE
            else -> Locale("en")
        }
        return locale.country
    }


    fun getLocale(language: String): Locale {
        return when {
            language.equals(Locales.Simplified_Chinese.country, true) -> Locale.SIMPLIFIED_CHINESE
            language.equals(Locales.Traditional_Chinese.country, true) -> Locale.TRADITIONAL_CHINESE
            else -> Locale("en")
        }
    }

    fun applyLanguage(context: Context, language: String): Context {
        var locale = getLocale(language)
//        if (LoginManager.getInstance(context).isAppLanguageSysDef()) {
//            locale = getLocale(Resources.getSystem().getConfiguration().locale.country)
//        }
        Log.e(
            "applyLanguage  ",
            "${context.javaClass.simpleName} ${context.javaClass.canonicalName}"
        )
        Locale.setDefault(locale)
        //val myLocale = getLocale(locale.country)
        Locale.setDefault(locale)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration

        conf.locale = locale
        conf.setLayoutDirection(locale)
        res.updateConfiguration(conf, dm)

        return context
    }
}
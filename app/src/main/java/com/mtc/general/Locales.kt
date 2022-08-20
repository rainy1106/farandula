package com.mtc.general

import java.util.*

object Locales {
    val SystemDefault: Locale by lazy { Locale("en","IN") }
    val English: Locale by lazy { Locale("en") }
    val Traditional_Chinese: Locale by lazy { Locale("zh","TW") }
    val Simplified_Chinese: Locale by lazy { Locale("zh","CN") }

    val RTL: Set<String> by lazy {
        hashSetOf(
            "ar", "dv", "fa", "ha", "he", "iw", "ji", "ps",
            "sd", "ug", "ur", "yi"
        )
    }
}
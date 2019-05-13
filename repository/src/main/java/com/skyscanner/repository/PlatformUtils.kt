package com.skyscanner.repository

import java.util.*

interface PlatformUtils {

    fun formatDate(format: String, inboundDate: Date, locale: String): String
    fun parseDate(format: String, input: String, timeZone: TimeZone): Date
    fun appendQueryUri(base: String, key: String, value: String): String

}
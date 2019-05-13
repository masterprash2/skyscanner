package com.skyscanner.interactor.mock

import android.net.Uri
import com.skyscanner.repository.PlatformUtils
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class MockPlatformImpl : PlatformUtils {

    override fun formatDate(format: String, date: Date,locale: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.forLanguageTag(locale))
        simpleDateFormat.timeZone = TimeZone.getTimeZone(locale)
        return simpleDateFormat.format(date)
    }

    override fun parseDate(format: String, input: String, timeZone: TimeZone): Date {
        val simpleDateFormat = SimpleDateFormat(format)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.parse(input)
    }

    override fun appendQueryUri(base: String, key: String, value: String): String {
        return Uri.parse(base).buildUpon().appendQueryParameter(key, value).build().toString()
    }


}
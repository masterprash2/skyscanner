package com.skyscanner.repository.impl

import android.net.Uri
import com.skyscanner.repository.PlatformUtils
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AndroidUtils @Inject constructor() : PlatformUtils {

    override fun formatDate(format: String, date: Date, locale: String): String {
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(date)
    }

    override fun parseDate(format: String, input: String, timeZone: TimeZone): Date {
        val simpleDateFormat = SimpleDateFormat(format)
        simpleDateFormat.timeZone = timeZone
        return simpleDateFormat.parse(input)
    }

    override fun appendQueryUri(base: String, key: String, value: String): String {
        if(key.isBlank()) {
            return base
        }
        return Uri.parse(base).buildUpon().appendQueryParameter(key, value).build().toString()
    }


}
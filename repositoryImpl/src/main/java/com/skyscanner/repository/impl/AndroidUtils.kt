package com.skyscanner.repository.impl

import android.net.Uri
import com.skyscanner.repository.PlatformUtils
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AndroidUtils @Inject constructor() : PlatformUtils {
    override fun delay(observable: Observable<*>, delay: Long, unit: TimeUnit): Observable<*>? {
        return observable.delay(delay, unit)
    }

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
        if (key.isBlank()) {
            return base
        }
        return Uri.parse(base).buildUpon().appendQueryParameter(key, value).build().toString()
    }


}
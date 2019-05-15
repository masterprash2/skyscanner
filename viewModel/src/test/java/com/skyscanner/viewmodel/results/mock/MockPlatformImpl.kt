package com.skyscanner.viewmodel.results.mock

import android.net.Uri
import com.skyscanner.repository.PlatformUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MockPlatformImpl : PlatformUtils {

    val delayPublisher = PublishSubject.create<Any>()

    override fun delay(observable: Observable<*>, delay: Long, unit: TimeUnit): Observable<*>? {
        return delayPublisher
    }

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
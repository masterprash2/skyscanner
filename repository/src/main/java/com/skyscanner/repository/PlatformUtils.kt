package com.skyscanner.repository

import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

interface PlatformUtils {

    fun formatDate(format: String, inboundDate: Date, locale: String): String
    fun parseDate(format: String, input: String, timeZone: TimeZone): Date
    fun appendQueryUri(base: String, key: String, value: String): String
    fun delay(observable: Observable<*>, delay: Long, unit: TimeUnit): Observable<*>?
}
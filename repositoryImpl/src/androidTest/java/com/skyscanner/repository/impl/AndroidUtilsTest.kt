package com.skyscanner.repository.impl

import androidx.test.runner.AndroidJUnit4
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.text.ParseException
import java.util.*

@RunWith(AndroidJUnit4::class)
class AndroidUtilsTest {

    val androidUtils: AndroidUtils = AndroidUtils()

    @Test
    fun formatDateValid() {
        val instance = Calendar.getInstance(Locale.getDefault())
        instance.timeZone = TimeZone.getTimeZone("GMT")
        instance.set(Calendar.YEAR,2019)
        instance.set(Calendar.HOUR_OF_DAY,14)
        instance.set(Calendar.MINUTE,2)
        instance.set(Calendar.SECOND,54)

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"))
        assertEquals("2019-14:02:54",androidUtils.formatDate("yyyy-HH:mm:ss",instance.time,Locale.getDefault().toLanguageTag()))
    }

    @Test
    fun formatDateLocaleConversion() {
        val instance = Calendar.getInstance(Locale.forLanguageTag("en-GB"))
        instance.timeZone = TimeZone.getTimeZone("en-GB")
        instance.set(Calendar.YEAR,2019)
        instance.set(Calendar.HOUR_OF_DAY,14)
        instance.set(Calendar.MINUTE,2)
        instance.set(Calendar.SECOND,54)
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+1:00"))
        assertEquals("2019-15:02:54",androidUtils.formatDate("yyyy-HH:mm:ss",instance.time,Locale.getDefault().toLanguageTag()))
    }


    @Test
    fun parseDate() {
        val instance = Calendar.getInstance(Locale.forLanguageTag("en-GB"))
        instance.timeZone = TimeZone.getTimeZone("GMT+1:00")
        instance.set(Calendar.YEAR,2019)
        instance.set(Calendar.MONTH,Calendar.MAY)
        instance.set(Calendar.DAY_OF_MONTH,10)
        instance.set(Calendar.HOUR_OF_DAY,15)
        instance.set(Calendar.MINUTE,20)
        instance.set(Calendar.SECOND,0)
        instance.set(Calendar.MILLISECOND,0)
        assertEquals(instance.time.time,androidUtils.parseDate("yyyy-MM-dd'T'HH:mm:ss","2019-05-10T15:20:00",TimeZone.getTimeZone("GMT+1:00")).time)
    }


    @Test
    fun parseDateDifferentTimezone() {
        val instance = Calendar.getInstance(Locale.forLanguageTag("en-GB"))
        instance.timeZone = TimeZone.getTimeZone("GMT+1:00")
        instance.set(Calendar.YEAR,2019)
        instance.set(Calendar.MONTH,Calendar.MAY)
        instance.set(Calendar.DAY_OF_MONTH,10)
        instance.set(Calendar.HOUR_OF_DAY,15)
        instance.set(Calendar.MINUTE,20)
        instance.set(Calendar.SECOND,0)
        instance.set(Calendar.MILLISECOND,0)
        assertEquals(instance.time.time,androidUtils.parseDate("yyyy-MM-dd'T'HH:mm:ss","2019-05-10T19:50:00",TimeZone.getTimeZone("GMT+5:30")).time)
    }

    @Test(expected = ParseException::class)
    fun parseDateInvalid() {
        androidUtils.parseDate("yyyy-MM-dd'T'HH:mm:ss","2019-05-10T19:50:a",TimeZone.getTimeZone("GMT+5:30"))
    }


    @Test
    fun appendUri() {
        assertEquals("http://test?location=value",androidUtils.appendQueryUri("http://test","location","value"))
        assertEquals("http://test?test=data&location=value",androidUtils.appendQueryUri("http://test?test=data","location","value"))
        assertEquals("http://test?test=data",androidUtils.appendQueryUri("http://test?test=data","","value"))
    }


}
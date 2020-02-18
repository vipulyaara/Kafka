package com.kafka.player.core

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Vipul Kumar; dated 28/03/19.
 */

private fun getTimeFromSecond(
    milliseconds: Long,
    timeFormat: String?,
    timeZone: String?
): String {
    val df = SimpleDateFormat(timeFormat, Locale.US)
    val calendar = GregorianCalendar()
    calendar.timeZone = TimeZone.getTimeZone(timeZone)
    calendar.timeInMillis = milliseconds
    calendar.add(Calendar.HOUR_OF_DAY, -5)
    calendar.add(Calendar.MINUTE, -30)
    return df.format(calendar.time)
}

private fun getTimefromSecondInLocalTime(
    milliseconds: Long,
    hour: Int,
    min: Int,
    seconds: Int,
    timeFormat: String?,
    timeZone: String?
): String {
    val df = SimpleDateFormat(timeFormat, Locale.US)
    val calendar = GregorianCalendar()
    calendar.timeZone = TimeZone.getTimeZone(timeZone)
    calendar.timeInMillis = milliseconds
    calendar.add(Calendar.HOUR_OF_DAY, hour)
    calendar.add(Calendar.MINUTE, min)
    calendar.add(Calendar.SECOND, seconds)
    return df.format(calendar.time)
}

fun getTimefromSecondISO(
    second: Long,
    hour: Int,
    min: Int,
    seconds: Int,
    timeFormat: String?,
    timeZone: String?
): Long {
    val df = SimpleDateFormat(timeFormat, Locale.UK)
    val calendar = GregorianCalendar()
    calendar.timeZone = TimeZone.getTimeZone(timeZone)
    calendar.timeInMillis = second * 1000
    calendar.add(Calendar.HOUR_OF_DAY, hour)
    calendar.add(Calendar.MINUTE, min)
    calendar.add(Calendar.SECOND, seconds)
    return calendar.timeInMillis / 1000L
}

fun getTimefromSecondISO(milliSecond: Long, timeFormat: String?, timeZone: String?): Long {
    val df = SimpleDateFormat(timeFormat, Locale.UK)
    val calendar = GregorianCalendar()
    calendar.timeZone = TimeZone.getTimeZone(timeZone)
    calendar.timeInMillis = milliSecond
    return calendar.timeInMillis / 1000L
}

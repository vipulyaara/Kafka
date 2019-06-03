package com.kafka.player.core

import com.kafka.player.extensions.logger
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

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
    logger.d("time : ${calendar.time}")
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
    logger.d("time : ${calendar.time}")
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
    logger.d(" current UTC Time: " + calendar.time)
    calendar.add(Calendar.HOUR_OF_DAY, hour)
    calendar.add(Calendar.MINUTE, min)
    calendar.add(Calendar.SECOND, seconds)
    logger.d(" current after GMT Time: " + calendar.time)
    logger.d(" current appstreet   " + df.format(calendar.time))
    return calendar.timeInMillis / 1000L
}

fun getTimefromSecondISO(milliSecond: Long, timeFormat: String?, timeZone: String?): Long {
    val df = SimpleDateFormat(timeFormat, Locale.UK)
    val calendar = GregorianCalendar()
    calendar.timeZone = TimeZone.getTimeZone(timeZone)
    calendar.timeInMillis = milliSecond
    logger.d(" current UTC Time: " + calendar.time)
    logger.d(" current after GMT Time: " + calendar.time)
    logger.d(" current appstreet   " + df.format(calendar.time))
    return calendar.timeInMillis / 1000L
}

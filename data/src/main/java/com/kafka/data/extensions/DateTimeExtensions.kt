package com.kafka.data.extensions

import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

fun Duration.formattedDuration() =
    arrayListOf(toHours(), toMinutes(), seconds).filterNotNull().joinToString(" : ")

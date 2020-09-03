package com.kafka.data.extensions

import org.threeten.bp.Duration

fun Duration.formattedDuration() =
    arrayListOf(toHours(), toMinutes(), seconds).joinToString(" : ")

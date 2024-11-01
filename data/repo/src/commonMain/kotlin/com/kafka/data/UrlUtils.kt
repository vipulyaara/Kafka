package com.kafka.data

import io.ktor.http.encodeURLParameter

fun String.encodeUrl(): String = encodeURLParameter()

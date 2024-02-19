package org.kafka.base.extensions

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

fun String.encodeUri(): String = android.net.Uri.encode(this)

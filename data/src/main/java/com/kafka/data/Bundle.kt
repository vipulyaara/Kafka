package com.kafka.data

import android.os.Bundle

fun Bundle.readable() = buildList {
    keySet().forEach {
        add("key=$it, value=${get(it)}")
    }
}.joinToString()

operator fun Bundle?.plus(other: Bundle?) =
    this.apply { (this ?: Bundle()).putAll(other ?: Bundle()) }

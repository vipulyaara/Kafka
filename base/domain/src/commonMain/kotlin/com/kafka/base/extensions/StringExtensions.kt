package com.kafka.base.extensions

fun String.isValidEmail(): Boolean {
    val emailRegex = EMAIL_REGEX.toRegex()
    return isNotEmpty() && emailRegex.matches(this)
}

const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

package com.kafka.data.extensions

inline fun <E : Any, T : Collection<E>> T?.letEmpty(func: (T) -> Unit): Unit {
    if (this != null && this.isNotEmpty()) {
        func(this)
    }
}

fun Int.millisecondsToMinutes() = this / 60_000

fun Int.formatMilliseconds(): String {
    val minutes = this / 1000 / 1000 / 60
    val seconds = this / 1000 / 1000 % 60

    return listOf(minutes, seconds).joinToString(" : ")
}

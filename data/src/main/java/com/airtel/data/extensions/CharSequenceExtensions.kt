package com.airtel.data.extensions

internal fun CharSequence?.isLongerThan(other: CharSequence?) =
    lengthOrZero() > other.lengthOrZero()

internal fun CharSequence?.lengthOrZero() = this?.length ?: 0

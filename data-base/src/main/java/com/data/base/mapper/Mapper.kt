package com.data.base.mapper

interface Mapper<F, T> {
    fun map(from: F): T
}

fun <F, T> Mapper<F, T>.toLambda(): suspend (F) -> T {
    return { map(it) }
}

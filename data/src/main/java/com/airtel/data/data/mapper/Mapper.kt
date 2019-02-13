package com.airtel.data.data.mapper

interface Mapper<F, T> {
    fun map(from: F): T
}

interface IndexedMapper<F, T> {
    fun map(index: Int, from: F): T
}

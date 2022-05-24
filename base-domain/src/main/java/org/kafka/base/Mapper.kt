package org.kafka.base

interface Mapper<F, T> {
    suspend fun map(from: F): T
}

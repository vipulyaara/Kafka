package com.kafka.data.data.db

interface DatabaseTransactionRunner {
    operator fun <T> invoke(run: () -> T): T
}

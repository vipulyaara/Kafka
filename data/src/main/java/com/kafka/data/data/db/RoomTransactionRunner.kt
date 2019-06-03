package com.kafka.data.data.db

import java.util.concurrent.Callable

internal class RoomTransactionRunner(private val db: MiddlewareDb) :
    DatabaseTransactionRunner {
    override operator fun <T> invoke(run: () -> T): T = db.runInTransaction(Callable<T> { run() })
}

package com.kafka.data.data.interactor

import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface PagingInteractor<T> {
    fun dataSourceFactory(): DataSource.Factory<Int, T>
}

fun <P> CoroutineScope.launchInteractor(interactor: Interactor<P>, param: P): Job {
    return launch(context = interactor.dispatcher, block = { interactor(param) })
}

fun CoroutineScope.launchInteractor(interactor: Interactor<Unit>) =
    launchInteractor(interactor, Unit)

interface Interactor<in P> {
    val dispatcher: CoroutineDispatcher
    suspend operator fun invoke(executeParams: P)
}

package com.kafka.data.data.interactor

import com.kafka.data.extensions.toFlowable
import io.reactivex.Flowable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.rx2.asObservable

abstract class ChannelInteractor<P, T : Any> : Interactor<P> {
    private val channel = Channel<T>()

    final override suspend fun invoke(executeParams: P) {
        channel.offer(execute(executeParams))
    }

    fun observe(): Flowable<T> = channel.asObservable(dispatcher).toFlowable()

    protected abstract suspend fun execute(executeParams: P): T

    fun clear() {
        channel.close()
    }
}

package com.kafka.user.feature.common

import com.airbnb.mvrx.*
import com.airbnb.mvrx.BaseMvRxViewModel
import com.kafka.data.extensions.e
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

/**
 * Simple ViewModel which exposes a [CompositeDisposable] and [Job] which are automatically cleared/stopped when
 * the ViewModel is cleared.
 */
open class BaseMvRxViewModel<S : MvRxState>(
    initialState: S
) : BaseMvRxViewModel<S>(initialState, debugMode = com.kafka.user.BuildConfig.DEBUG) {
    private val job = Job()

    protected suspend inline fun <T> Flow<T>.execute(
        crossinline stateReducer: S.(Async<T>) -> S
    ) = execute({ it }, stateReducer)

    protected suspend inline fun <T, V> Flow<T>.execute(
        crossinline mapper: (T) -> V,
        crossinline stateReducer: S.(Async<V>) -> S
    ) {
        setState { stateReducer(Loading()) }

        @Suppress("USELESS_CAST")
        return map { Success(mapper(it)) as Async<V> }
            .catch {
                if (com.kafka.user.BuildConfig.DEBUG) {
                    e(it) {"Exception during observe"}
                    throw RuntimeException("Exception during observe in BaseViewModel")
                }
                emit(Fail(it))
            }
            .collect { setState { stateReducer(it) } }
    }
}

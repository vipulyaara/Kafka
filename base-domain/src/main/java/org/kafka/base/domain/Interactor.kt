package org.kafka.base.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {
    suspend operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus> {
        return flow {
            try {
                withTimeout(timeoutMs) {
                    emit(InvokeStarted)
                    doWork(params)
                    emit(InvokeSuccess)
                }
            } catch (t: TimeoutCancellationException) {
                emit(InvokeError(t))
            }
        }.catch { t ->
            Timber.e(t.localizedMessage ?: t.toString())
            emit(InvokeError(t))
        }
    }

    suspend fun execute(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(10)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SubjectInteractor<P, T> {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    operator fun invoke(params: P): Flow<T> {
        paramState.tryEmit(params)
        return flow
    }

    fun execute(params: P): Flow<T> = createObservable(params)

    abstract fun createObservable(params: P): Flow<T>

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    suspend fun get(): T = flow.first()
    suspend fun getOrNull(): T? = flow.firstOrNull()

    private val errorState = MutableSharedFlow<Throwable>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    protected fun onError(error: Throwable) {
        errorState.tryEmit(error)
    }

    fun errors(): Flow<Throwable> = errorState.asSharedFlow()
}

abstract class SuspendingWorkInteractor<P : Any, T> : SubjectInteractor<P, T>() {
    override fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): T
}

operator fun <T> SubjectInteractor<Unit, T>.invoke() = invoke(Unit)

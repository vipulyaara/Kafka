package com.data.base

import com.data.base.extensions.e
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {
    protected abstract val scope: CoroutineScope

    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus> {
        val channel = ConflatedBroadcastChannel<InvokeStatus>(
            InvokeStatus.Loading
        )
        scope.launch {
            try {
                withTimeout(timeoutMs) {
                    try {
                        doWork(params)
                        channel.send(InvokeStatus.Success)
                    } catch (t: Throwable) {
                        e(t) { t.localizedMessage ?: "exception" }

                        if (t is UnknownHostException) {
                            channel.send(InvokeStatus.Error(RuntimeException("Please check your internet connection")))
                        } else {
                            channel.send(InvokeStatus.Error(t))
                        }
                    }
                }
            } catch (t: TimeoutCancellationException) {
                e(t) { t.localizedMessage ?: "timeout handle" }
                channel.send(InvokeStatus.Error(t))
            }
        }
        return channel.asFlow()
    }

    suspend fun executeSync(params: P) {
        scope.launch { doWork(params) }.join()
    }

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

interface ObservableInteractor<T> {
    val dispatcher: CoroutineDispatcher
    fun observe(): Flow<T>
}

abstract class SuspendingWorkInteractor<P : Any, T : Any> : ObservableInteractor<T> {
    private val channel = ConflatedBroadcastChannel<T>()
    suspend operator fun invoke(params: P) = channel.send(doWork(params))
    abstract suspend fun doWork(params: P): T
    override fun observe(): Flow<T> = channel.asFlow().distinctUntilChanged()
}

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<R> {
        return flow {
            emit(doWork(params))
        }
    }

    protected abstract suspend fun doWork(params: P): R
}

abstract class SyncWorkUseCase<P : Any, T : Any> {
    abstract fun doWork(params: P): T
    operator fun invoke(params: P) = doWork(params)
}

abstract class SubjectInteractor<P : Any, T> : ObservableInteractor<T> {
    private val channel = ConflatedBroadcastChannel<P>()

    operator fun invoke(params: P) = channel.sendBlocking(params)

    protected abstract fun createObservable(params: P): Flow<T>

    override fun observe(): Flow<T> = channel.asFlow()
        .distinctUntilChanged()
        .flatMapLatest {
            createObservable(it)
        }
}

operator fun Interactor<Unit>.invoke() = invoke(Unit)
operator fun <T> SubjectInteractor<Unit, T>.invoke() = invoke(Unit)

fun <I : ObservableInteractor<T>, T> CoroutineScope.launchObserve(
    interactor: I,
    f: suspend (Flow<T>) -> Unit
) {
    launch(interactor.dispatcher) {
        f(interactor.observe())
    }
}

package org.rekhta.base.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import com.dropbox.android.external.store4.get
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.rekhta.base.errorLog
import timber.log.Timber
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {
    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus> {
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

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(10)
    }
}

abstract class SuspendInteractor<in P> {
    abstract val scope: CoroutineScope

    operator fun invoke(params: P) {
        scope.launch {
            try {
                doWork(params)
            } catch (e: Exception) {
                errorLog { "SuspendInteractor cancelled" }
            }
        }
    }

    abstract suspend fun doWork(params: P)
}


abstract class PagingInteractor<P : Any, T : Any> :
    SubjectInteractor<P, PagingData<T>>() {
    interface Parameters<T : Any> {
        val pagingConfig: PagingConfig
    }

    companion object {
        val DEFAULT_PAGING_CONFIG = PagingConfig(
            pageSize = 50,
            initialLoadSize = 50,
            prefetchDistance = 5,
            enablePlaceholders = true
        )
    }
}

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<InvokeResponse<R>> {
        return flow {
            emit(InvokeResponse.Loading(ResponseOrigin.Fetcher))
            emit(InvokeResponse.Data(doWork(params), ResponseOrigin.Fetcher))
        }.flowOn(Dispatchers.IO).catch { t ->
            Timber.e(t.localizedMessage ?: t.toString())
            emit(InvokeResponse.Error.Exception(t, ResponseOrigin.Fetcher))
        }
    }

    protected abstract suspend fun doWork(params: P): R
}

abstract class SubjectInteractor<P : Any, T> {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    fun observe() = flow

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract suspend fun createObservable(params: P): Flow<T>

    protected fun onError(error: Throwable) {
        Timber.e(error)
//        errorState.tryEmit(error)
    }
}

//abstract class SubjectInteractor<P : Any, T> {
//    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
//    // suspending. This means that we can't suspend while flatMapLatest cancels any
//    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
//    // instead, resulting in mostly the same result.
//    private val paramState = MutableSharedFlow<P>(
//        replay = 1,
//        extraBufferCapacity = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//
//    operator fun invoke(params: P) {
//        Timber.d("invoked")
//        paramState.tryEmit(params)
//    }
//
//    protected abstract fun createObservable(params: P): Flow<T>
//
//    val flow: Flow<T> = paramState
//        .distinctUntilChanged { old, new -> old == new }
//        .flatMapLatest {
//            Timber.d("flatMapLatest $it")
//            createObservable(it).catch {
//                onError(it)
//            }
//        }
//        .distinctUntilChanged()
//
//    fun observe() = flow
//
//    private val errorState = MutableSharedFlow<Throwable>(
//        replay = 1,
//        extraBufferCapacity = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//
//    protected fun onError(error: Throwable) {
//        Timber.e(error)
//        errorState.tryEmit(error)
//    }
//
//    fun errors(): Flow<Throwable> = errorState.asSharedFlow()
//}

abstract class StoreInteractor<P : Any, T> {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<InvokeResponse<T?>>

    @ExperimentalCoroutinesApi
    val flow: Flow<InvokeResponse<T?>>
        get() = paramState.flatMapLatest { createObservable(it) }
}

operator fun <T> SubjectInteractor<Unit, T>.invoke() = invoke(Unit)

suspend inline fun <Key : Any, Output : Any> Store<Key, Output>.fetch(
    key: Key,
    forceFresh: Boolean = false
): Output = when {
    // If we're forcing a fresh fetch, do it now
    forceFresh -> fresh(key)
    else -> get(key)
}

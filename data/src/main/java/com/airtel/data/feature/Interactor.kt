package com.airtel.data.feature

import androidx.paging.DataSource
import com.airtel.data.extensions.toFlowable
import com.airtel.data.model.data.ErrorResult
import com.airtel.data.model.data.Result
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.asObservable

interface Interactor<in P> {
    val dispatcher: CoroutineDispatcher
    suspend operator fun invoke(executeParams: P)
}

interface PagingInteractor<T> {
    fun dataSourceFactory(): DataSource.Factory<Int, T>
}

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

abstract class SubjectInteractor<P : Any, EP, T> : Interactor<EP> {
    private var disposable: Disposable? = null
    private val subject: BehaviorSubject<T> = BehaviorSubject.create()

    private val loading = BehaviorSubject.createDefault(false)

    private lateinit var params: P

    private fun throwError(exception: Exception) {
        subject.onError(exception)
        throw exception
    }

    fun setParams(params: P) {
        this.params = params
        setSource(createObservable(params))
    }

    fun observe(): Flowable<T> = subject.toFlowable()

    final override suspend fun invoke(executeParams: EP) {
        loading.onNext(true)
        val result = execute(params, executeParams)
        if (result is ErrorResult) throwError(result.exception)
        loading.onNext(false)
    }

    protected abstract suspend fun execute(params: P, executeParams: EP): Result<*>

    protected abstract fun createObservable(params: P): Flowable<T>

    fun clear() {
        disposable?.dispose()
        disposable = null
    }

    fun observable(): Observable<T> = subject.toFlowable().toObservable()

    private fun setSource(source: Flowable<T>) {
        disposable?.dispose()
        disposable = source.subscribe(subject::onNext, subject::onError)
    }
}

fun <P> CoroutineScope.launchInteractor(interactor: Interactor<P>, param: P): Job {
    return launch(context = interactor.dispatcher, block = { interactor(param) })
}

fun CoroutineScope.launchInteractor(interactor: Interactor<Unit>) =
    launchInteractor(interactor, Unit)

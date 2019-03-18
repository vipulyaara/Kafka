package com.kafka.data.data.interactor

import com.kafka.data.extensions.toFlowable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class FirebaseInteractor<P : Any, EP, T> : Interactor<EP> {
    private var disposable: Disposable? = null
    private val subject: BehaviorSubject<T> =
        BehaviorSubject.create()

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
        loading.onNext(false)
    }

    protected abstract suspend fun execute(params: P, executeParams: EP)

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

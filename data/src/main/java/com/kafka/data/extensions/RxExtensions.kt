package com.kafka.data.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions

internal fun <T> Maybe<T>.emptySubscribe() =
    subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER)

internal fun <T> Single<T>.emptySubscribe() =
    subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER)

internal fun <T> Flowable<T>.emptySubscribe() =
    subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER)

internal fun <T> Observable<T>.emptySubscribe() =
    subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER)

internal fun Completable.emptySubscribe() =
    subscribe(Functions.EMPTY_ACTION, Functions.ERROR_CONSUMER)

fun <T> Observable<T>.toFlowable() = toFlowable(BackpressureStrategy.LATEST)!!

internal fun <T> emptyFlowableList() = Flowable.just(emptyList<T>())

fun Disposable.disposeOnDestroy(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(DisposingLifecycleObserver(this))
}

class DisposingLifecycleObserver(private val disposable: Disposable) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.dispose()
    }
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable?) {
    disposable?.let { add(it) }
}

package com.kafka.user.ui

import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.config.logging.Logger
import com.kafka.data.extensions.toFlowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.kodein.di.generic.instance

class RxLoadingCounter {
    private var loaders = 0
    private val loadingState = BehaviorSubject.createDefault(loaders)
    private val logger: Logger by kodeinInstance.instance()

    val observable: Observable<Boolean>
        get() = loadingState.map { it > 0 }

    val flowable by lazy(LazyThreadSafetyMode.NONE) { observable.toFlowable() }

    fun addLoader() {
        logger.d("add loader called")
        loadingState.onNext(++loaders)
    }

    fun removeLoader() {
        logger.d("remove loader called")
        loadingState.onNext(--loaders)
    }
}

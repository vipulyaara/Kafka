package com.airtel.kafka.model

import android.annotation.SuppressLint
import androidx.lifecycle.MediatorLiveData
import com.airtel.data.feature.Interactor
import com.airtel.data.feature.launchInteractor
import com.airtel.data.model.data.AtvError
import com.airtel.data.model.data.Resource
import com.airtel.data.model.data.Status
import com.airtel.data.util.AppRxSchedulers
import com.airtel.kafka.util.RxLoadingCounter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope

/**
 * @author Vipul Kumar; dated 18/12/18.
 *
 * LiveData to be returned to client.
 */
@SuppressLint("CheckResult")
class ResourceLiveData<T>(private val schedulers: AppRxSchedulers) :
    MediatorLiveData<Resource<T>>() {
    private var resource: Resource<T> = Resource.create(null as T?, Status.LOADING, null)
    private val loadingState = RxLoadingCounter()

    fun resetResource() {
        value = Resource.create(null as T?, Status.LOADING, null)
    }

    fun loading(): Disposable? {
        return loadingState.flowable.toObservable()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe {
                resource = if (it) {
                    Resource.create(resource.data, Status.LOADING, null)
                } else {
                    Resource.create(resource.data, Status.SUCCESS, null)
                }
            }
    }

    fun data(
        observable: Observable<T>
    ): Disposable? {
        return observable
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe {
                value = Resource.create(it, resource.status, resource.error)
            }
    }

    private fun error(throwable: Throwable) {
        value = Resource.create(resource.data, resource.status,
            AtvError()
        )
    }

    fun <P> launchInteractor(scope: CoroutineScope, interactor: Interactor<P>, params: P) {
        loadingState.addLoader()
        scope.launchInteractor(interactor, params)
            .invokeOnCompletion { throwable ->
                loadingState.removeLoader()
                throwable?.let {
                    error(it)
                }
            }
    }

    fun resource() {
    }
}

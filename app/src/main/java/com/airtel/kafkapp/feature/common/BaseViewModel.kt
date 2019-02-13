package com.airtel.kafkapp.feature.common

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.airtel.data.config.kodeinInstance
import com.airtel.data.config.logging.Logger
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 22/10/18.
 *
 * ViewModel which exposes a [CompositeDisposable] and [Job]
 * which are automatically cleared/stopped when the ViewModel is cleared.
 *
 * Ideally, a code that triggers a change in viewState looks like
 *
 * flowable.toObservable()
 * .subscribeOn(schedulers.io)
 * .execute { state }
 *
 * the fragments can then flowable [observableState] and listen to any changes.
 */
open class BaseViewModel<S : BaseViewState>(
    var state: S
) : ViewModel(), IBaseViewModel {
    private val job = Job()

    protected val logger: Logger by kodeinInstance.instance()
    override val disposables = CompositeDisposable()
    override val scope = CoroutineScope(Dispatchers.Main + job)
    var observableState = MediatorLiveData<S>()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        job.cancel()
    }

    /** posts a new state value to [observableState] which is usually observed by the fragment. */
    fun <T> Observable<T>.execute(
        stateReducer: S.(T) -> S
    ): Disposable {
        return map { stateReducer.invoke(state, it) }
            .subscribe {
                state = it
                observableState.postValue(it)
            }
    }
}

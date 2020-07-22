package com.kafka.ui_common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kafka.ui_common.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

open class BaseViewModel<ViewState : BaseViewState>(private var s: ViewState) : ViewModel() {
    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    fun setState(reducer: ViewState.() -> ViewState) {
        s = reducer(s)
        _viewState.postValue(s)
    }

    protected suspend inline fun <T> Flow<T>.execute(
        crossinline stateReducer: ViewState.(T) -> ViewState
    ) {
        return map { it }
            .catch {
                if (BuildConfig.DEBUG) {
                    throw it
                }
            }
            .collect { setState { stateReducer(it) } }
    }
}


suspend inline fun <T, R> Flow<T>.execute(
    liveData: MutableLiveData<R>,
    crossinline stateReducer: R.(T) -> R
) {
    return map { it }
        .catch {
            if (BuildConfig.DEBUG) {
                throw it
            }
        }
        .collect { liveData.postValue(stateReducer(liveData.value!!, it)) }
}

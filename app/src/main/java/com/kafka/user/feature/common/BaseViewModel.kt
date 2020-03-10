package com.kafka.user.feature.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kafka.data.extensions.e
import com.kafka.data.model.data.ErrorResult
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success
import com.kafka.ui.BaseViewState
import com.kafka.user.BuildConfig
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
        crossinline stateReducer: ViewState.(Success<T>) -> ViewState
    ) {
        return map { Success(it) }
            .catch {
                if (BuildConfig.DEBUG) {
                    e(it) { "Exception during observe" }
                    throw it
                }
                // TODO: handle error
            }
            .collect { setState { stateReducer(it) } }
    }
}

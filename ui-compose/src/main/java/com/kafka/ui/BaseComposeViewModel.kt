package com.kafka.ui

import androidx.lifecycle.ViewModel
import com.kafka.ui_common.base.BaseViewState
import com.kafka.ui_common.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

open class BaseComposeViewModel<ViewState : BaseViewState>(var viewState: ViewState) : ViewModel() {

    fun setState(reducer: ViewState.() -> Unit) {
        with(viewState) { reducer() }
    }

    protected suspend inline fun <T> Flow<T>.execute(crossinline stateReducer: ViewState.(T) -> Unit) {
        return map { it }
            .catch {
                if (BuildConfig.DEBUG) {
                    throw it
                }
            }.collect { setState { viewState.stateReducer(it) } }
    }
}

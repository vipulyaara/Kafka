package com.airtel.kafka.feature.common

import androidx.lifecycle.ViewModel
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.data.config.logging.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.generic.instance

/**
 * Simple ViewModel which exposes a [CompositeDisposable] and [Job]
 * which are automatically cleared/stopped when the ViewModel is cleared.
 */
internal open class BaseViewModel : ViewModel(),
    IVisionViewModel {
    private val job = Job()
    protected val logger: Logger by kodeinInstance.instance()

    override val disposables = CompositeDisposable()
    override val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        job.cancel()
    }

    /**
     * Adds the disposable to [disposables] which is cleared when viewModel clears.
     */
    protected fun Disposable.disposeOnClear(): Disposable {
        disposables += this
        return this
    }
}

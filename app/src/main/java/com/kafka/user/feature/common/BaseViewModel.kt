package com.kafka.user.feature.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.config.logging.Logger
import com.kafka.user.BuildConfig
import com.kafka.user.KafkaApplication
import io.reactivex.disposables.CompositeDisposable
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
 */
open class BaseViewModel<S : BaseViewState>(
    state: S
) : ViewModel(),  IBaseViewModel {

    private val job = Job()

    protected val logger: Logger by kodeinInstance.instance()
    override val disposables = CompositeDisposable()
    override val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        job.cancel()
    }
}

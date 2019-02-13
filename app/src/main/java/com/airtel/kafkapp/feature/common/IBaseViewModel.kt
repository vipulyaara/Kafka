package com.airtel.kafkapp.feature.common

import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope

/**
 * @author Vipul Kumar; dated 22/10/18.
 *
 */
interface IBaseViewModel {
    val disposables: CompositeDisposable
    val scope: CoroutineScope
}

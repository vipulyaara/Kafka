package com.airtel.kafka.feature.common

import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope

internal interface IVisionViewModel {
    val disposables: CompositeDisposable
    val scope: CoroutineScope
}

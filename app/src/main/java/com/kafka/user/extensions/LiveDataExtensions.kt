package com.kafka.user.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import io.reactivex.processors.PublishProcessor
import rx.subjects.PublishSubject

/**
 * @author Vipul Kumar; dated 17/01/19.
 */

inline fun <T> LiveData<T>.observeK(
    owner: LifecycleOwner,
    crossinline observer: (T?) -> Unit
) {
    this.observe(owner, Observer { observer(it) })
}

inline fun <T> LiveData<T>.switchMap(crossinline func: (T) -> LiveData<T>): LiveData<T>? {
    return Transformations.switchMap(this) { func.invoke(it) }
}

inline fun <Request, Response> LiveData<Request>.map(crossinline func: (Request) -> Response): LiveData<Response> {
    return Transformations.map(this) { func.invoke(it) }
}

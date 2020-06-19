package com.kafka.data.extensions

import com.dropbox.android.external.store4.StoreResponse

inline fun  <E: Any, T: Collection<E>> T?.letEmpty(func: (T) -> Unit): Unit {
    if (this != null && this.isNotEmpty()) {
        func(this)
    }
}


fun <T> StoreResponse<T>.isLoading() = (this as? StoreResponse.Loading) != null

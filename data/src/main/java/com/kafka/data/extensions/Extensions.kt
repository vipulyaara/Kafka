package com.kafka.data.extensions

inline fun  <E: Any, T: Collection<E>> T?.letEmpty(func: (T) -> Unit): Unit {
    if (this != null && this.isNotEmpty()) {
        func(this)
    }
}

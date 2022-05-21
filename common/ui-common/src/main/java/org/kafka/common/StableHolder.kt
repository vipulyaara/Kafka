package org.kafka.common

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.rekhta.base.network.model.content.T

@Stable
class StableHolder<T>(val item: T) {
    operator fun component1(): T = item
}

@Immutable
class ImmutableHolder<T>(val item: T) {
    operator fun component1(): T = item
}

@Immutable
class ImmutableList<T>(val items: List<T>) {
    operator fun component1(): List<T> = items
}

fun <T : Any> List<T>.asImmutable() = ImmutableList(this)

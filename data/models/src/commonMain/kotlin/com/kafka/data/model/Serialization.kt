@file:Suppress("ktlint:standard:filename")

package com.kafka.data.model

import kotlin.reflect.KClass

data class SerializationPolymorphicDefaultPair<T : Any>(
    val base: KClass<T>,
    val default: KClass<out T>,
)

package com.kafka.networking

import kotlin.reflect.KClass

data class SerializationPolymorphicDefaultPair<T : Any>(
    val base: KClass<T>,
    val default: KClass<out T>,
)

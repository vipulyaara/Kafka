package com.kafka.user.ui.animation

import android.os.Build
import android.util.FloatProperty
import android.util.Property

/**
 * A delegate for creating a [Property] of `float` type.
 */
abstract class FloatProp<T>(val name: String) {
    abstract operator fun set(o: T, value: Float)
    abstract operator fun get(o: T): Float
}

fun <T> createFloatProperty(impl: FloatProp<T>): Property<T, Float> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        object : FloatProperty<T>(impl.name) {
            override fun get(o: T): Float = impl[o]

            override fun setValue(o: T, value: Float) {
                impl[o] = value
            }
        }
    } else {
        object : Property<T, Float>(Float::class.java, impl.name) {
            override fun get(o: T): Float = impl[o]

            override fun set(o: T, value: Float) {
                impl[o] = value
            }
        }
    }
}

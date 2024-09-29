@file:Suppress("ktlint:standard:filename")

package org.kafka.base

import me.tatarka.inject.annotations.Qualifier
import me.tatarka.inject.annotations.Scope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProcessLifetime

@Scope
annotation class ApplicationScope

@Scope
annotation class ActivityScope

@Qualifier
@Target(
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE
)
annotation class Named(val value: String)

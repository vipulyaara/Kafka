@file:Suppress("ktlint:standard:filename")
package org.kafka.base

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProcessLifetime

package com.kafka.data.injection

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProcessLifetime

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class Authenticated

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Initializers

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ForStore

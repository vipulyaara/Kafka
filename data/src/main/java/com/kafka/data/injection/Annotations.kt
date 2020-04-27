package com.kafka.data.injection

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ApplicationId

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProcessLifetime

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class Authenticated

@Qualifier
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Initializers

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsLowRamDevice

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Customer

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher

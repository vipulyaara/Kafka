package com.kafka.data.data.config

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class PerActivity

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class PerApplication

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ApplicationId

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class ProcessLifetime

@Qualifier
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Initializers

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IsLowRamDevice

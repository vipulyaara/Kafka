package com.kafka.data.data.api

/**
 * Created by VipulKumar on 03/04/18.
 * Annotation to specify if a Retrofit request needs to be authenticated.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class RequestPolicy(
    val authenticated: Boolean = false,
    val msisdnAuthentication: Boolean = false
)

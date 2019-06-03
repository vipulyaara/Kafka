package com.kafka.data.data.annotations

/**
 * @author Vipul Kumar; dated 19/12/18.
 *
 * Annotation to denote that a class should be used as a provider and not singleton.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UseProvider

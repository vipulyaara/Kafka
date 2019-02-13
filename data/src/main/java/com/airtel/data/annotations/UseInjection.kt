package com.airtel.data.annotations

/**
 * @author Vipul Kumar; dated 19/12/18.
 *
 * Annotation to denote that a class should be used from dependency injection (currently Kodein).
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UseInjection

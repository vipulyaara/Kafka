package com.kafka.data.extensions

/**
 * @author Vipul Kumar; dated 22/01/19.
 */
internal val <T> T.exhaustive: T
    get() = this

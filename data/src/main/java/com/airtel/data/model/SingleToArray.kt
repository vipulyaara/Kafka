package com.airtel.data.model

import com.squareup.moshi.JsonQualifier

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@JsonQualifier
annotation class SingleToArray

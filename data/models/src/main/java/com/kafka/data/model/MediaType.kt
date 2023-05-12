package com.kafka.data.model

sealed class MediaType {
    object Text : MediaType()
    object Audio : MediaType()
    object Video : MediaType()
}

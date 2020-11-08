package com.kafka.data.model.model

sealed class MediaType {
    object Text : MediaType()
    object Audio : MediaType()
    object Video : MediaType()
}

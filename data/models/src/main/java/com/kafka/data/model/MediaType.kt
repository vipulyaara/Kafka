package com.kafka.data.model

sealed class MediaType {
    data object Text : MediaType()
    data object Audio : MediaType()
}

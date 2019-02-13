package com.airtel.data.model

sealed class MediaType {
    object Text : MediaType()
    object Audio : MediaType()
    object Video : MediaType()
}

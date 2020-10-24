package com.kafka.data.api

import javax.inject.Inject

interface EndpointDelegate {
    fun getEndpoint(): String
}

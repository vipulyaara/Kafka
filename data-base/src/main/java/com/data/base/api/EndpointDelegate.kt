package com.data.base.api

import javax.inject.Inject

interface EndpointDelegate {
    fun getEndpoint(): String
}

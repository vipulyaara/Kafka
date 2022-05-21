package org.kafka.common.common

interface BaseViewState {
    companion object {
        val Empty
            get() = object : BaseViewState {}
    }
}

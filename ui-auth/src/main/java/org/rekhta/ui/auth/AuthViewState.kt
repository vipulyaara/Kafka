package org.rekhta.ui.auth

import com.kafka.data.entities.User

data class AuthViewState(
    val currentUser: User? = null,
    val isLoading: Boolean = false
)

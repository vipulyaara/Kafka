package com.kafka.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.kafka.base.domain.InvokeError
import org.kafka.domain.interactors.account.SignInAnonymously
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val signInAnonymously: SignInAnonymously
) : ViewModel() {

    fun signInAnonymously() {
        viewModelScope.launch {
            signInAnonymously(Unit).collect {
                // todo: assume it will succeed
                if (it is InvokeError) {
                    throw it.throwable
                }
            }
        }
    }
}

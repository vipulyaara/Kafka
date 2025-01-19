package com.kafka.kms.ui.text

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.debug
import com.kafka.kms.domain.PrepareEpubUseCase
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

sealed class PrepareState {
    object Initial : PrepareState()
    object Loading : PrepareState()
    data class Success(val message: String) : PrepareState()
    data class Error(val message: String) : PrepareState()
}

@Inject
class PrepareEpubViewModel(
    private val prepareEpubUseCase: PrepareEpubUseCase
) : ViewModel() {
    private val _prepareState = mutableStateOf<PrepareState>(PrepareState.Initial)
    val prepareState: State<PrepareState> = _prepareState

    fun prepareEpub(opfPath: String, xhtmlPath: String) {
        viewModelScope.launch {
            try {
                _prepareState.value = PrepareState.Loading
                prepareEpubUseCase(PrepareEpubUseCase.Params(opfPath, xhtmlPath))
                _prepareState.value = PrepareState.Success("Successfully prepared epub files")
            } catch (e: Exception) {
                debug { "PrepareEpubViewModel error: ${e.message}" }
                _prepareState.value = PrepareState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetState() {
        _prepareState.value = PrepareState.Initial
    }
} 
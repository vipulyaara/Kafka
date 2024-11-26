package com.kafka.kms.ui.librivox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.kms.data.models.LibrivoxSection
import com.kafka.kms.domain.UpdateLibrivoxBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class LibrivoxViewModel(
    private val updateLibrivoxBook: UpdateLibrivoxBook
) : ViewModel() {
    private val _state = MutableStateFlow(LibrivoxState())
    val state: StateFlow<LibrivoxState> = _state.asStateFlow()

    var itemId by mutableStateOf("")
        internal set

    fun fetchItem() {
        if (itemId.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            
            try {
                updateLibrivoxBook(itemId)
                // The actual item details will be loaded from the database by another component
                _state.update { it.copy(loading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }
}

data class LibrivoxState(
    val loading: Boolean = false,
    val error: String? = null,
    val itemDetail: LibrivoxItem? = null
)

data class LibrivoxItem(
    val id: String,
    val title: String,
    val author: String,
    val language: String,
    val totalSections: Int,
    val sections: List<LibrivoxSection>
)

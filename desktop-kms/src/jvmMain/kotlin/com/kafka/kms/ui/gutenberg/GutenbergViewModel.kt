package com.kafka.kms.ui.gutenberg

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.debug
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.data.entities.Download
import com.kafka.data.entities.ItemDetail
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.kms.domain.UpdateGutenbergBook
import com.kafka.kms.domain.gutenbergItemId
import com.kafka.networking.localizedMessage
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class GutenbergViewModel(
    private val observeItemDetail: ObserveItemDetail,
    private val updateGutenbergBook: UpdateGutenbergBook,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    var bookId by mutableStateOf("")

    var state = combine(
        observeItemDetail.flow,
        updateGutenbergBook.inProgress,
    ) { itemDetail, loading ->
        GutenbergState(itemDetail = itemDetail, loading = loading)
    }.stateInDefault(viewModelScope, GutenbergState())

    fun fetchBook() {
        viewModelScope.launch {
            debug { "Get gutenberg book $bookId" }
            observeItemDetail(ObserveItemDetail.Param(gutenbergItemId(bookId)))
            updateGutenbergBook(bookId).onException {
                snackbarManager.addMessage(it.localizedMessage())
            }
        }
    }
}

data class GutenbergState(
    val itemDetail: ItemDetail? = null,
    val download: Download? = null,
    val loading: Boolean = false,
)

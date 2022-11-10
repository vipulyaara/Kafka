package org.kafka.item.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.domain.interactors.ToggleFavorite
import org.kafka.domain.interactors.UpdateItemDetail
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.observers.ObserveItemDetail
import org.kafka.domain.observers.ObserveItemFollowStatus
import org.kafka.domain.observers.ObserveQueryItems
import org.kafka.navigation.DeepLinksNavigations
import org.kafka.navigation.Navigation
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val updateItemDetail: UpdateItemDetail,
    private val observeItemDetail: ObserveItemDetail,
    private val observeQueryItems: ObserveQueryItems,
    private val updateItems: UpdateItems,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    private val toggleFavorite: ToggleFavorite,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    val state: StateFlow<ItemDetailViewState> = combine(
        observeItemDetail.flow.onEach { observeByAuthor(it) },
        observeQueryItems.flow.onStart { emit(emptyList()) },
        observeItemFollowStatus.flow,
        loadingState.observable,
        uiMessageManager.message,
    ) { itemDetail, itemsByCreator, isFavorite, isLoading, message ->
        debug { "item detail results $isLoading $message" }
        ItemDetailViewState(
            itemDetail = itemDetail,
            itemsByCreator = itemsByCreator.filterNot { it.itemId == itemId },
            isFavorite = isFavorite,
            isLoading = isLoading,
            message = message
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ItemDetailViewState(),
    )

    init {
        refresh()
    }

    fun retry() {
        clearMessage()
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            updateItemDetail(UpdateItemDetail.Param(itemId))
                .collectStatus(loadingState, uiMessageManager)
        }

        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeItemFollowStatus(ObserveItemFollowStatus.Params(itemId))
    }

    fun updateFavorite() {
        updateFavorite(state.value.itemDetail!!.itemId)
    }

    private fun updateFavorite(itemId: String) {
        viewModelScope.launch {
            toggleFavorite(ToggleFavorite.Params(itemId)).collect()
        }
    }

    private fun observeByAuthor(itemDetail: ItemDetail?) {
        itemDetail?.let {
            debug { "observe query for creator ${itemDetail.creator}" }
            observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))
            itemDetail.creator?.let { ArchiveQuery().booksByAuthor(it) }?.let {
                observeQueryItems(ObserveQueryItems.Params(it))
                viewModelScope.launch {
                    updateItems(UpdateItems.Params(it)).collect()
                }
            }
        }
    }

    fun shareItemText(): String {
        val itemTitle = state.value.itemDetail!!.title

        return """
            Check out $itemTitle on Kafka
            ${DeepLinksNavigations.findUri(Navigation.ItemDetail(itemId))}
        """.trimIndent()
    }

    private fun clearMessage() {
        viewModelScope.launch {
            state.value.message?.id?.let { uiMessageManager.clearMessage(it) }
        }
    }
}

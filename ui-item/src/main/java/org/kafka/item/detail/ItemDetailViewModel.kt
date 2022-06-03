package org.kafka.item.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import com.kafka.ui_common.navigation.DeepLinksNavigations
import com.kafka.ui_common.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.common.common.collect
import org.kafka.common.common.viewModelScoped
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.domain.interactors.*
import org.kafka.domain.observers.ObserveItemDetail
import org.kafka.domain.observers.ObserveItemFollowStatus
import org.kafka.domain.observers.ObserveQueryItems
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val updateItemDetail: UpdateItemDetail,
    observeItemDetail: ObserveItemDetail,
    private val observeQueryItems: ObserveQueryItems,
    private val updateItems: UpdateItems,
    private val startFileDownload: StartFileDownload,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    private val toggleFavorite: ToggleFavorite,
    private val addRecentItem: AddRecentItem,
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
        debug { "item detail results $isLoading" }
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
        viewModelScoped {
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
            toggleFavorite(ToggleFavorite.Params(itemId)).collect { }
        }
    }

    fun addRecentItem() {
        viewModelScoped {
            addRecentItem(AddRecentItem.Params(state.value.itemDetail!!.itemId)).collect { }
        }
    }

    private fun observeByAuthor(itemDetail: ItemDetail?) {
        itemDetail?.let {
            debug { "observe query for creator ${itemDetail.creator}" }
            observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))
            itemDetail.creator?.let { ArchiveQuery().booksByAuthor(it) }?.let {
                observeQueryItems(ObserveQueryItems.Params(it))
                viewModelScoped {
                    updateItems(UpdateItems.Params(it)).collect { }
                }
            }
        }
    }

    fun shareItemText(): String {
        val itemTitle = state.value.itemDetail!!.title

        return """
            $itemTitle
            I really liked this book on the Kafka app. I think you will enjoy it.
            
            ${DeepLinksNavigations.findUri(Navigation.ItemDetail(itemId))}
        """.trimIndent()
    }

    suspend fun download(readerUrl: String) {
        addRecentItem()
        startFileDownload(readerUrl).collectStatus(loadingState, uiMessageManager)
    }
}

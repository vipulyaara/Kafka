package org.kafka.item.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.getTextFile
import com.kafka.data.entities.isAudio
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import com.sarahang.playback.core.PlaybackConnection
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
import org.kafka.common.asUiMessage
import org.kafka.common.collectStatus
import org.kafka.domain.interactors.AddRecentItem
import org.kafka.domain.interactors.ToggleFavorite
import org.kafka.domain.interactors.UpdateItemDetail
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.observers.ObserveItemDetail
import org.kafka.domain.observers.ObserveItemFollowStatus
import org.kafka.domain.observers.ObserveQueryItems
import org.kafka.navigation.DeepLinksNavigation
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.Navigation
import org.kafka.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val updateItemDetail: UpdateItemDetail,
    private val observeItemDetail: ObserveItemDetail,
    private val observeQueryItems: ObserveQueryItems,
    private val updateItems: UpdateItems,
    private val addRecentItem: AddRecentItem,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    private val toggleFavorite: ToggleFavorite,
    private val playbackConnection: PlaybackConnection,
    private val navigator: Navigator,
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

    fun onPrimaryAction(itemId: String) {
        val itemDetail = state.value.itemDetail
        if (itemDetail.isAudio()) {
            addRecentItem(itemId)
            playbackConnection.playAlbum(itemId)
        } else {
            openReader(itemDetail, itemId)
        }
    }

    private fun openReader(itemDetail: ItemDetail?, itemId: String) {
        itemDetail?.getTextFile()?.let {
            addRecentItem(itemId)
            navigator.navigate(LeafScreen.Reader.buildRoute(it, navigator.currentRoot.value))
        } ?: viewModelScope.launch {
            uiMessageManager.emitMessage("PDFs are not supported".asUiMessage())
        }
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
            observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))

            itemDetail.creator?.let { ArchiveQuery().booksByAuthor(it) }?.let {
                observeQueryItems(ObserveQueryItems.Params(it))
                viewModelScope.launch {
                    updateItems(UpdateItems.Params(it)).collectStatus(
                        counter = loadingState,
                        uiMessageManager = uiMessageManager
                    )
                }
            }
        }
    }

    private fun addRecentItem(itemId: String) {
        viewModelScope.launch {
            addRecentItem(AddRecentItem.Params(itemId)).collect()
        }
    }

    val shareItemText: String
        get() {
            val itemTitle = state.value.itemDetail!!.title

            return """
            Check out $itemTitle on Kafka
            
            ${DeepLinksNavigation.findUri(Navigation.ItemDetail(itemId))}
        """.trimIndent()
        }

    private fun clearMessage() {
        viewModelScope.launch {
            state.value.message?.id?.let { uiMessageManager.clearMessage(it) }
        }
    }
}

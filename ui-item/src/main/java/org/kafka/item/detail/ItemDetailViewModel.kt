package org.kafka.item.detail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.ItemDetail
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
import org.kafka.analytics.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.common.collectStatus
import org.kafka.common.shareText
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.interactors.AddRecentItem
import org.kafka.domain.interactors.UpdateItemDetail
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.interactors.account.UpdateFavorite
import org.kafka.domain.observers.ObserveItemDetail
import org.kafka.domain.observers.ObserveItemFollowStatus
import org.kafka.domain.observers.ObserveQueryItems
import org.kafka.item.R
import org.kafka.navigation.DeepLinksNavigation
import org.kafka.navigation.DynamicDeepLinkHandler
import org.kafka.navigation.Navigation
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val updateItemDetail: UpdateItemDetail,
    private val observeItemDetail: ObserveItemDetail,
    private val observeQueryItems: ObserveQueryItems,
    private val updateItems: UpdateItems,
    private val addRecentItem: AddRecentItem,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    private val updateFavorite: UpdateFavorite,
    private val playbackConnection: PlaybackConnection,
    private val navigator: Navigator,
    private val dynamicDeepLinkHandler: DynamicDeepLinkHandler,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
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
        ItemDetailViewState(
            itemDetail = itemDetail,
            itemsByCreator = itemsByCreator.filterNot { it.itemId == itemId },
            isFavorite = isFavorite,
            isLoading = isLoading,
            message = message,
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ItemDetailViewState(),
    )

    init {
        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeItemFollowStatus(ObserveItemFollowStatus.Params(itemId))
        observeItemFollowStatus(ObserveItemFollowStatus.Params(itemId))

        refresh()
    }

    fun retry() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            updateItemDetail(UpdateItemDetail.Param(itemId))
                .collectStatus(loadingState, snackbarManager)
        }
    }

    fun onPrimaryAction(itemId: String) {
        val itemDetail = state.value.itemDetail
        if (itemDetail.isAudio()) {
            addRecentItem(itemId)
            playbackConnection.playAlbum(itemId)
        } else {
            openReader(itemId)
        }
    }


    fun openFiles(itemId: String) {
        analytics.log { this.openFiles(itemId) }
        navigator.navigate(Screen.Files.createRoute(navigator.currentRoot.value, itemId))
    }

    private fun openReader(itemId: String) {
        val itemDetail = state.value.itemDetail
        itemDetail?.primaryTextFile?.let {
            addRecentItem(itemId)
            navigator.navigate(Screen.Reader.createRoute(navigator.currentRoot.value, it))
        } ?: viewModelScope.launch {
            snackbarManager.addMessage(UiMessage(R.string.file_type_is_not_supported))
        }
    }

    fun updateFavorite() {
        viewModelScope.launch {
            updateFavorite(UpdateFavorite.Params(itemId, !state.value.isFavorite)).collect()
        }
    }

    private fun observeByAuthor(itemDetail: ItemDetail?) {
        itemDetail?.let {
            observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))

            itemDetail.creator?.let { ArchiveQuery().booksByAuthor(it) }?.let {
                observeQueryItems(ObserveQueryItems.Params(it))
                viewModelScope.launch {
                    updateItems(UpdateItems.Params(it)).collectStatus(loadingState, snackbarManager)
                }
            }
        }
    }

    private fun addRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { this.addRecentItem(itemId) }
            addRecentItem(AddRecentItem.Params(itemId)).collect()
        }
    }

    fun shareItemText(context: Context) {
        analytics.log { this.shareItem(itemId) }
        val itemTitle = state.value.itemDetail!!.title

        val link = DeepLinksNavigation.findUri(Navigation.ItemDetail(itemId)).toString()
        val deepLink = dynamicDeepLinkHandler.createDeepLinkUri(link)
        val text = """
            Check out $itemTitle on Kafka
            
            $deepLink
        """.trimIndent()

        context.shareText(text)
    }
}

package com.kafka.item.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.combine
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.extensions.getActivity
import com.kafka.common.platform.ShareUtils
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.model.SearchFilter.Creator
import com.kafka.data.model.SearchFilter.Subject
import com.kafka.data.prefs.ItemReadCounter
import com.kafka.domain.interactors.ResumeAlbum
import com.kafka.domain.interactors.UpdateCreatorItems
import com.kafka.domain.interactors.UpdateFavorite
import com.kafka.domain.interactors.UpdateItemDetail
import com.kafka.domain.interactors.recent.AddRecentItem
import com.kafka.domain.interactors.recent.IsResumableAudio
import com.kafka.domain.observers.ObserveCreatorItems
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.ObservePrimaryFile
import com.kafka.domain.observers.ShouldUseOnlineReader
import com.kafka.domain.observers.library.ObserveFavoriteStatus
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.Config
import com.kafka.navigation.deeplink.DeepLinks
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.ItemDescription
import com.kafka.navigation.graph.Screen.OnlineReader
import com.kafka.navigation.graph.Screen.Search
import com.kafka.navigation.graph.encodeUrl
import com.kafka.play.AppReviewManager
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.borrowableBookMessage
import com.kafka.remote.config.isAppReviewPromptEnabled
import com.kafka.remote.config.isItemDetailDynamicThemeEnabled
import com.kafka.remote.config.isShareEnabled
import com.kafka.remote.config.isSummaryEnabled
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadByItemId
import javax.inject.Inject

class ItemDetailViewModel @Inject constructor(
    observeItemDetail: ObserveItemDetail,
    observeDownloadByItemId: ObserveDownloadByItemId,
    isResumableAudio: IsResumableAudio,
    shouldUseOnlineReader: ShouldUseOnlineReader,
    observePrimaryFile: ObservePrimaryFile,
    @Assisted savedStateHandle: SavedStateHandle,
    private val updateItemDetail: UpdateItemDetail,
    private val observeCreatorItems: ObserveCreatorItems,
    private val updateCreatorItems: UpdateCreatorItems,
    private val addRecentItem: AddRecentItem,
    private val observeFavoriteStatus: ObserveFavoriteStatus,
    private val updateFavorite: UpdateFavorite,
    private val resumeAlbum: ResumeAlbum,
    private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    private val appReviewManager: AppReviewManager,
    private val itemReadCounter: ItemReadCounter,
    private val shareUtils: ShareUtils
) : ViewModel() {
    private val itemId: String = savedStateHandle.get<String>("itemId")!!

    val state: StateFlow<ItemDetailViewState> = combine(
        observeItemDetail.flow.onEach { item ->
            updateItemsByCreator(item?.creator)
        },
        observeCreatorItems.flow,
        observeFavoriteStatus.flow,
        combine(
            updateItemDetail.inProgress,
            updateCreatorItems.inProgress
        ) { loadingStates ->
            loadingStates.any { loading -> loading }
        },
        observeDownloadByItemId.flow,
        isResumableAudio.flow,
        shouldUseOnlineReader.flow,
        observePrimaryFile.flow
    ) { itemDetail, itemsByCreator, isFavorite, loading,
        downloadItem, isResumableAudio, useOnlineReader, primaryFile ->
        ItemDetailViewState(
            itemDetail = itemDetail,
            itemsByCreator = itemsByCreator,
            isFavorite = isFavorite,
            isLoading = loading,
            downloadItem = downloadItem,
            ctaText = itemDetail?.let { ctaText(itemDetail, isResumableAudio) }.orEmpty(),
            isDynamicThemeEnabled = remoteConfig.isItemDetailDynamicThemeEnabled(),
            borrowableBookMessage = remoteConfig.borrowableBookMessage(),
            isSummaryEnabled = remoteConfig.isSummaryEnabled(),
            useOnlineReader = useOnlineReader,
            primaryFile = primaryFile
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ItemDetailViewState(),
    )

    init {
        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeFavoriteStatus(ObserveFavoriteStatus.Params(itemId))
        isResumableAudio(IsResumableAudio.Params(itemId))
        shouldUseOnlineReader(ShouldUseOnlineReader.Param(itemId))
        observePrimaryFile(ObservePrimaryFile.Param(itemId))

        observeDownloadByItemId(
            ObserveDownloadByItemId.Params(
                itemId = itemId,
                statuses = listOf(DownloadStatus.COMPLETED)
            )
        )

        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            updateItemDetail(UpdateItemDetail.Param(itemId))
                .onException { snackbarManager.addMessage("Failed to update details") }
        }

        observeCreatorItems(ObserveCreatorItems.Params(itemId))
        observeFavoriteStatus(ObserveFavoriteStatus.Params(itemId))
    }

    fun onPrimaryAction(itemId: String) {
        itemReadCounter.incrementItemOpenCount()

        if (state.value.itemDetail!!.isAudio) {
            addRecentItem(itemId)
            viewModelScope.launch { resumeAlbum(itemId) }
        } else {
            openReader(itemId)
        }
    }

    fun openFiles(itemId: String) {
        analytics.log { this.openFiles(itemId) }
        navigator.navigate(Screen.Files(itemId))
    }

    private fun openReader(itemId: String) {
        val itemDetail = state.value.itemDetail
        val primaryFile = state.value.primaryFile

        if (primaryFile != null && itemDetail != null) {
            addRecentItem(primaryFile.fileId)

            if (state.value.useOnlineReader) {
                logOnlineReader(itemDetail = itemDetail)
                navigator.navigate(OnlineReader(itemDetail.itemId, primaryFile.fileId))
            } else {
                if (primaryFile.isEpub) {
                    analytics.log { readItem(itemId = itemId, type = "offline") }
                    navigator.navigate(Screen.EpubReader(primaryFile.fileId))
                } else {
                    analytics.log { readItem(itemId = itemId, type = "offline") }
                    navigator.navigate(Screen.PdfReader(primaryFile.fileId))
                }
            }
        } else {
            analytics.log { fileNotSupported(itemId = itemId) }
            viewModelScope.launch {
                snackbarManager.addMessage(
                    UiMessage.Plain("File type is not supported")
                )
            }
        }
    }

    fun updateFavorite() {
        viewModelScope.launch {
            updateFavorite(UpdateFavorite.Params(itemId, !state.value.isFavorite))
        }
    }

    private fun updateItemsByCreator(creator: String?) {
        creator?.let { query ->
            viewModelScope.launch {
                updateCreatorItems(UpdateCreatorItems.Params(query))
            }
        }
    }

    private fun addRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { this.addRecentItem(itemId) }
            addRecentItem(AddRecentItem.Params(itemId))
        }
    }

    fun openItemDetail(itemId: String, source: String) {
        analytics.log { this.openItemDetail(itemId = itemId, source = source) }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun goToSubjectSubject(keyword: String) {
        analytics.log { this.openSubject(keyword, "item_detail") }
        navigator.navigate(Search(keyword, Subject.name), RootScreen.Search)
    }

    fun goToCreator(keyword: String?) {
        analytics.log { this.openCreator(name = keyword, source = "item_detail") }
        navigator.navigate(Search(keyword.orEmpty(), Creator.name), RootScreen.Search)
    }

    fun openItemDescription(itemId: String) {
        navigator.navigate(ItemDescription(itemId))
    }

    fun isShareEnabled() = remoteConfig.isShareEnabled() && state.value.itemDetail != null

    fun shareItemText(context: Any?) {
        analytics.log { this.shareItem(itemId, "item_detail") }
        val itemTitle = state.value.itemDetail!!.title

        val link = DeepLinks.find(Screen.ItemDetail(itemId))
        val text = "\nCheck out $itemTitle on Kafka\n\n$link\n"

        shareUtils.shareText(text = text, context = context)
    }

    fun openArchiveItem() {
        analytics.log { this.openArchiveItem(itemId) }
        navigator.navigate(Screen.Web(Config.archiveDetailUrl(itemId).encodeUrl()))
    }

    fun openSummary(itemId: String) {
        analytics.log { this.openSummary(itemId) }
        navigator.navigate(Screen.Summary(itemId))
    }

    fun showAppRatingIfNeeded(context: Any?) {
        if (remoteConfig.isAppReviewPromptEnabled()) {
            if (itemReadCounter.totalItemOpens % itemOpenThresholdForAppReview == 0) {
                getActivity(context)?.let { appReviewManager.showReviewDialog(it) }
            }
        }
    }

    private fun logOnlineReader(itemDetail: ItemDetail) {
        analytics.log {
            readItem(
                itemId = itemDetail.itemId,
                type = "online",
                isRestrictedAccess = itemDetail.isAccessRestricted
            )
        }
    }

    private fun ctaText(itemDetail: ItemDetail, isResumableAudio: Boolean) =
        if (itemDetail.isAudio) {
            if (isResumableAudio) {
                "Resume"
            } else {
                "Play"
            }
        } else {
            if (itemDetail.isAccessRestricted) {
                "Borrow"
            } else {
                "Read"
            }
        }
}

private const val itemOpenThresholdForAppReview = 20
const val itemDetailSourceCreator = "item_detail/creator"

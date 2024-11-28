package com.kafka.item.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.extensions.getActivity
import com.kafka.common.platform.ShareUtils
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.BookshelfDefaults.default
import com.kafka.data.entities.ItemDetail
import com.kafka.data.prefs.ItemReadCounter
import com.kafka.domain.interactors.GetPrimaryFile
import com.kafka.domain.interactors.ResumeAlbum
import com.kafka.domain.interactors.UpdateCreatorItems
import com.kafka.domain.interactors.UpdateItemDetail
import com.kafka.domain.interactors.library.AddToBookshelf
import com.kafka.domain.interactors.recent.AddRecentItem
import com.kafka.domain.interactors.recent.IsResumableAudio
import com.kafka.domain.observers.ObserveCreatorItems
import com.kafka.domain.observers.ObserveItem
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.library.ObserveDefaultListStatus
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.DeepLinks
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.ItemDescription
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import com.kafka.navigation.graph.Screen.Search
import com.kafka.play.AppReviewManager
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isAppReviewPromptEnabled
import com.kafka.remote.config.isItemDetailDynamicThemeEnabled
import com.kafka.remote.config.isShareEnabled
import com.kafka.remote.config.isSummaryEnabled
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ItemDetailViewModel(
    observeItemDetail: ObserveItemDetail,
    observeItem: ObserveItem,
    isResumableAudio: IsResumableAudio,
    getPrimaryFile: GetPrimaryFile,
    @Assisted savedStateHandle: SavedStateHandle,
    private val updateItemDetail: UpdateItemDetail,
    private val observeCreatorItems: ObserveCreatorItems,
    private val updateCreatorItems: UpdateCreatorItems,
    private val addRecentItem: AddRecentItem,
    private val observeDefaultListStatus: ObserveDefaultListStatus,
    private val addToBookshelf: AddToBookshelf,
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
    private val origin = Origin.find(savedStateHandle.get<String>("origin"))

    val creatorItems = observeCreatorItems.flow.stateInDefault(viewModelScope, emptyList())
    val itemPlaceholder = observeItem.flow.map { it.asPlaceholder(origin) }
        .stateInDefault(viewModelScope, null)

    val state: StateFlow<ItemDetailViewState> = combine(
        observeItemDetail.flow.onEach { item -> updateItemsByCreator(item?.creator) },
        observeDefaultListStatus.flow,
        combine(
            updateItemDetail.inProgress,
            updateCreatorItems.inProgress
        ) { loadingStates ->
            loadingStates.any { loading -> loading }
        },
        isResumableAudio.flow
    ) { itemDetail, isFavorite, loading, isResumableAudio ->
        ItemDetailViewState(
            itemDetail = itemDetail,
            isFavorite = isFavorite,
            isLoading = loading,
            ctaText = itemDetail?.let { ctaText(itemDetail, isResumableAudio) }.orEmpty(),
            isDynamicThemeEnabled = remoteConfig.isItemDetailDynamicThemeEnabled(),
            isSummaryEnabled = remoteConfig.isSummaryEnabled(),
            shareEnabled = remoteConfig.isShareEnabled() && itemDetail != null,
            primaryFile = getPrimaryFile(itemId).getOrNull()
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ItemDetailViewState(),
    )

    init {
        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeItem(itemId)
        observeDefaultListStatus(ObserveDefaultListStatus.Params(itemId))
        isResumableAudio(IsResumableAudio.Params(itemId))

        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            updateItemDetail(UpdateItemDetail.Param(itemId))
                .onException { snackbarManager.addMessage("Failed to update details") }
        }

        observeDefaultListStatus(ObserveDefaultListStatus.Params(itemId))
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

    private fun openReader(itemId: String) {
        val itemDetail = state.value.itemDetail
        val primaryFile = state.value.primaryFile

        if (primaryFile != null && itemDetail != null) {
            addRecentItem(itemId)

            analytics.log { readItem(itemId = itemId, type = "offline") }
            navigator.navigate(Screen.EpubReader(itemId, primaryFile.fileId))
        } else {
            analytics.log { fileNotSupported(itemId = itemId) }
            viewModelScope.launch {
                snackbarManager.addMessage(
                    UiMessage.Plain("File type is not supported")
                )
            }
        }
    }

    fun updateBookshelfStatus() {
        viewModelScope.launch {
            addToBookshelf(AddToBookshelf.Params(itemId, default, !state.value.isFavorite))
        }
    }

    fun openAddToBookshelf() {
        navigator.navigate(Screen.AddToBookshelf(itemId))
    }

    private fun updateItemsByCreator(creator: String?) {
        creator?.let { query ->
            observeCreatorItems(ObserveCreatorItems.Params(itemId, query))
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
        navigator.navigate(Search(keyword), RootScreen.Search)
    }

    fun goToCreator(keyword: String?) {
        analytics.log { this.openCreator(name = keyword, source = "item_detail") }
        navigator.navigate(Search(keyword.orEmpty()), RootScreen.Search)
    }

    fun openItemDescription(itemId: String) {
        navigator.navigate(ItemDescription(itemId))
    }

    fun shareItemText(context: Any?) {
        analytics.log { this.shareItem(itemId, "item_detail") }
        val itemTitle = state.value.itemDetail!!.title

        val link = DeepLinks.find(Screen.ItemDetail(itemId))
        val text = "\nCheck out $itemTitle on Kafka\n\n$link\n"

        shareUtils.shareText(text = text, context = context)
    }

    fun openSummary(itemId: String) {
        analytics.log { this.openSummary(itemId) }
        navigator.navigate(Screen.Summary(itemId))
    }

    fun openReportContent() {
        analytics.log { this.openReportContent(itemId) }
        navigator.navigate(Screen.ReportContent(itemId))
    }

    fun showAppRatingIfNeeded(context: Any?) {
        if (remoteConfig.isAppReviewPromptEnabled()) {
            if (itemReadCounter.totalItemOpens % itemOpenThresholdForAppReview == 0) {
                getActivity(context)?.let { appReviewManager.showReviewDialog(it) }
            }
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
            "Read"
        }
}

private const val itemOpenThresholdForAppReview = 20
const val itemDetailSourceCreator = "item_detail/creator"

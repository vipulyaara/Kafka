package org.kafka.item.detail

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.SearchFilter.Creator
import com.kafka.data.model.SearchFilter.Subject
import com.kafka.data.model.booksByAuthor
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isOnlineReaderEnabled
import com.kafka.remote.config.isRelatedContentRowEnabled
import com.kafka.remote.config.isShareEnabled
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.kafka.analytics.AppReviewManager
import org.kafka.analytics.logger.Analytics
import org.kafka.base.combine
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.collectStatus
import org.kafka.common.shareText
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.interactors.ResumeAlbum
import org.kafka.domain.interactors.UpdateFavorite
import org.kafka.domain.interactors.UpdateItemDetail
import org.kafka.domain.interactors.UpdateItems
import org.kafka.domain.interactors.recent.AddRecentItem
import org.kafka.domain.interactors.recent.IsResumableAudio
import org.kafka.domain.interactors.recommendation.GetRelatedContent
import org.kafka.domain.interactors.recommendation.PostRecommendationEvent
import org.kafka.domain.interactors.recommendation.PostRecommendationEvent.RecommendationEvent
import org.kafka.domain.observers.ObserveCreatorItems
import org.kafka.domain.observers.ObserveItemDetail
import org.kafka.domain.observers.library.ObserveDownloadByItemId
import org.kafka.domain.observers.library.ObserveFavoriteStatus
import org.kafka.item.R
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import org.kafka.navigation.Screen.ItemDescription
import org.kafka.navigation.Screen.OnlineReader
import org.kafka.navigation.Screen.Reader
import org.kafka.navigation.Screen.Search
import org.kafka.navigation.deeplink.Config
import org.kafka.navigation.deeplink.DeepLinksNavigation
import org.kafka.navigation.deeplink.Navigation
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    observeItemDetail: ObserveItemDetail,
    observeDownloadByItemId: ObserveDownloadByItemId,
    isResumableAudio: IsResumableAudio,
    private val updateItemDetail: UpdateItemDetail,
    private val observeCreatorItems: ObserveCreatorItems,
    private val updateItems: UpdateItems,
    private val addRecentItem: AddRecentItem,
    private val observeFavoriteStatus: ObserveFavoriteStatus,
    private val updateFavorite: UpdateFavorite,
    private val postRecommendationEvent: PostRecommendationEvent,
    private val getRelatedContent: GetRelatedContent,
    private val resumeAlbum: ResumeAlbum,
    private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    private val appReviewManager: AppReviewManager,
    private val application: Application,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])
    private val loadingState = ObservableLoadingCounter()
    private val currentRoot
        get() = navigator.currentRoot.value

    var recommendedContent by mutableStateOf(emptyList<Item>())

    val state: StateFlow<ItemDetailViewState> = combine(
        observeItemDetail.flow.onEach { item ->
            updateItemsByCreator(item?.creator)
            updateRelatedItems(item?.itemId)
        },
        observeCreatorItems.flow,
        observeFavoriteStatus.flow,
        loadingState.observable,
        observeDownloadByItemId.flow,
        isResumableAudio.flow
    ) { itemDetail, itemsByCreator, isFavorite, isLoading, downloadItem, isResumableAudio ->
        ItemDetailViewState(
            itemDetail = itemDetail,
            itemsByCreator = itemsByCreator,
            isFavorite = isFavorite,
            isLoading = isLoading,
            downloadItem = downloadItem,
            ctaText = itemDetail?.let { ctaText(itemDetail, isResumableAudio) }.orEmpty(),
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ItemDetailViewState(),
    )

    init {
        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeFavoriteStatus(ObserveFavoriteStatus.Params(itemId))
        isResumableAudio(IsResumableAudio.Params(itemId))
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
                .collectStatus(loadingState, snackbarManager)
        }

        observeCreatorItems(ObserveCreatorItems.Params(itemId))
        observeFavoriteStatus(ObserveFavoriteStatus.Params(itemId))
    }

    fun onPrimaryAction(itemId: String) {
        if (state.value.itemDetail!!.isAudio) {
            addRecentItem(itemId)
            analytics.log { playItem(itemId) }
            viewModelScope.launch { resumeAlbum(itemId).collect() }
        } else {
            openReader(itemId)
        }

        viewModelScope.launch {
            postRecommendationEvent.invoke(RecommendationEvent.UseContent(itemId)).collect()
        }
    }

    fun openFiles(itemId: String) {
        analytics.log { this.openFiles(itemId) }
        navigator.navigate(Screen.Files.createRoute(navigator.currentRoot.value, itemId))
    }

    private fun openReader(itemId: String) {
        val itemDetail = state.value.itemDetail

        if (itemDetail?.primaryFile != null) {
            addRecentItem(itemId)

            if (state.value.downloadItem == null && remoteConfig.isOnlineReaderEnabled()) {
                analytics.log { readItem(itemId = itemId, type = "online") }
                navigator.navigate(OnlineReader.createRoute(currentRoot, itemDetail.itemId))
            } else {
                analytics.log { readItem(itemId = itemId, type = "offline") }
                navigator.navigate(Reader.createRoute(currentRoot, itemDetail.primaryFile!!))
            }
        } else {
            analytics.log { fileNotSupported(itemId = itemId) }
            viewModelScope.launch {
                snackbarManager.addMessage(UiMessage(R.string.file_type_is_not_supported))
            }
        }
    }

    fun updateFavorite() {
        viewModelScope.launch {
            updateFavorite(UpdateFavorite.Params(itemId, !state.value.isFavorite)).collect()
        }
    }

    private fun updateItemsByCreator(creator: String?) {
        creator?.let { ArchiveQuery().booksByAuthor(it) }?.let { query ->
            viewModelScope.launch {
                updateItems(UpdateItems.Params(query))
                    .collectStatus(loadingState, snackbarManager)
            }
        }
    }

    private fun updateRelatedItems(itemId: String?) {
        if (itemId != null && remoteConfig.isRelatedContentRowEnabled()) {
            viewModelScope.launch {
                recommendedContent = getRelatedContent(itemId).getOrNull().orEmpty()
            }
        }
    }

    private fun addRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { this.addRecentItem(itemId) }
            addRecentItem(AddRecentItem.Params(itemId)).collect()
        }
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId) }
        navigator.navigate(Screen.ItemDetail.createRoute(currentRoot, itemId))
    }

    fun goToSubjectSubject(keyword: String) {
        analytics.log { this.openSubject(keyword, "item_detail") }
        navigator.navigate(Search.createRoute(RootScreen.Search, keyword, Subject.name))
    }

    fun goToCreator(keyword: String?) {
        navigator.navigate(Search.createRoute(RootScreen.Search, keyword, Creator.name))
    }

    fun showDescription(itemId: String) {
        navigator.navigate(ItemDescription.createRoute(currentRoot, itemId))
    }

    fun isShareEnabled() = remoteConfig.isShareEnabled() && state.value.itemDetail != null

    fun shareItemText(context: Context) {
        analytics.log { this.shareItem(itemId, "item_detail") }
        val itemTitle = state.value.itemDetail!!.title

        val link = DeepLinksNavigation.findUri(Navigation.ItemDetail(itemId)).toString()
        val text = context.getString(R.string.check_out_on_kafka, itemTitle, link).trimIndent()

        context.shareText(text)
    }

    fun openArchiveItem() {
        analytics.log { this.openArchiveItem(itemId) }
        navigator.navigate(Screen.Web.createRoute(currentRoot, Config.archiveDetailUrl(itemId)))
    }

    fun showAppRatingIfNeeded(context: Context) {
        viewModelScope.launch { appReviewManager.incrementItemOpenCount() }

        if (appReviewManager.totalItemOpens % itemOpenThresholdForAppReview == 0) {
            context.getActivity()?.let { appReviewManager.showReviewDialog(it) }
        }
    }

    private fun Context.getActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

    private fun ctaText(itemDetail: ItemDetail, isResumableAudio: Boolean) =
        if (itemDetail.isAudio) {
            if (isResumableAudio) {
                application.getString(R.string.resume)
            } else {
                application.getString(R.string.play)
            }
        } else {
            application.getString(R.string.read)
        }
}

private const val itemOpenThresholdForAppReview = 20

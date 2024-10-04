package org.kafka.item.detail

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.SearchFilter.Creator
import com.kafka.data.model.SearchFilter.Subject
import com.kafka.data.model.booksByAuthor
import com.kafka.data.prefs.ItemReadCounter
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.borrowableBookMessage
import com.kafka.remote.config.isItemDetailDynamicThemeEnabled
import com.kafka.remote.config.isShareEnabled
import com.kafka.remote.config.isSummaryEnabled
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import com.kafka.analytics.logger.Analytics
import com.kafka.base.combine
import com.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.collectStatus
import org.kafka.common.shareText
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import com.kafka.domain.interactors.ResumeAlbum
import com.kafka.domain.interactors.UpdateFavorite
import com.kafka.domain.interactors.UpdateItemDetail
import com.kafka.domain.interactors.UpdateItems
import com.kafka.domain.interactors.recent.AddRecentItem
import com.kafka.domain.interactors.recent.IsResumableAudio
import com.kafka.domain.observers.ObserveCreatorItems
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.ShouldUseOnlineReader
import com.kafka.domain.observers.library.ObserveFavoriteStatus
import org.kafka.item.R
import org.kafka.navigation.Navigator
import org.kafka.navigation.deeplink.Config
import org.kafka.navigation.deeplink.DeepLinksNavigation
import org.kafka.navigation.deeplink.Navigation
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.graph.Screen
import org.kafka.navigation.graph.Screen.ItemDescription
import org.kafka.navigation.graph.Screen.OnlineReader
import org.kafka.navigation.graph.Screen.Reader
import org.kafka.navigation.graph.Screen.Search
import org.kafka.navigation.graph.encodeUrl
import com.kafka.play.AppReviewManager
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadByItemId
import javax.inject.Inject

class ItemDetailViewModel @Inject constructor(
    observeItemDetail: ObserveItemDetail,
    observeDownloadByItemId: ObserveDownloadByItemId,
    isResumableAudio: IsResumableAudio,
    @Assisted savedStateHandle: SavedStateHandle,
    shouldUseOnlineReader: ShouldUseOnlineReader,
    private val updateItemDetail: UpdateItemDetail,
    private val observeCreatorItems: ObserveCreatorItems,
    private val updateItems: UpdateItems,
    private val addRecentItem: AddRecentItem,
    private val observeFavoriteStatus: ObserveFavoriteStatus,
    private val updateFavorite: UpdateFavorite,
    private val resumeAlbum: ResumeAlbum,
    private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    private val appReviewManager: AppReviewManager,
    private val application: Application,
    private val itemReadCounter: ItemReadCounter,
) : ViewModel() {
    private val itemId: String = savedStateHandle.toRoute<Screen.ItemDetail>().itemId
    private val loadingState = ObservableLoadingCounter()

    val state: StateFlow<ItemDetailViewState> = combine(
        observeItemDetail.flow.onEach { item ->
            updateItemsByCreator(item?.creator)
        },
        observeCreatorItems.flow,
        observeFavoriteStatus.flow,
        loadingState.observable,
        observeDownloadByItemId.flow,
        isResumableAudio.flow,
        shouldUseOnlineReader.flow
    ) { itemDetail, itemsByCreator, isFavorite, isLoading,
        downloadItem, isResumableAudio, useOnlineReader ->
        ItemDetailViewState(
            itemDetail = itemDetail,
            itemsByCreator = itemsByCreator,
            isFavorite = isFavorite,
            isLoading = isLoading,
            downloadItem = downloadItem,
            ctaText = itemDetail?.let { ctaText(itemDetail, isResumableAudio) }.orEmpty(),
            isDynamicThemeEnabled = remoteConfig.isItemDetailDynamicThemeEnabled(),
            borrowableBookMessage = remoteConfig.borrowableBookMessage(),
            isSummaryEnabled = remoteConfig.isSummaryEnabled() && (itemDetail?.isText ?: false),
            useOnlineReader = useOnlineReader
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
        itemReadCounter.incrementItemOpenCount()

        if (state.value.itemDetail!!.isAudio) {
            addRecentItem(itemId)
            viewModelScope.launch { resumeAlbum(itemId).collect() }
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

        if (itemDetail?.primaryFile != null) {
            addRecentItem(itemId)

            if (state.value.useOnlineReader) {
                logOnlineReader(itemDetail = itemDetail)
                navigator.navigate(OnlineReader(itemDetail.itemId, itemDetail.primaryFile!!))
            } else {
                analytics.log { readItem(itemId = itemId, type = "offline") }
                navigator.navigate(Reader(itemDetail.primaryFile!!))
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

    private fun addRecentItem(itemId: String) {
        viewModelScope.launch {
            analytics.log { this.addRecentItem(itemId) }
            addRecentItem(AddRecentItem.Params(itemId)).collect()
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
        analytics.log { this.openCreator("item_detail") }
        navigator.navigate(Search(keyword.orEmpty(), Creator.name), RootScreen.Search)
    }

    fun openItemDescription(itemId: String) {
        navigator.navigate(ItemDescription(itemId))
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
        navigator.navigate(Screen.Web(Config.archiveDetailUrl(itemId).encodeUrl()))
    }

    fun openSummary(itemId: String) {
        analytics.log { this.openSummary(itemId) }
        navigator.navigate(Screen.Summary(itemId))
    }

    fun showAppRatingIfNeeded(context: Context) {
        if (itemReadCounter.totalItemOpens % itemOpenThresholdForAppReview == 0) {
            context.getActivity()?.let { appReviewManager.showReviewDialog(it) }
        }
    }

    private fun Context.getActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
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
                application.getString(R.string.resume)
            } else {
                application.getString(R.string.play)
            }
        } else {
            if (itemDetail.isAccessRestricted) {
                application.getString(R.string.borrow)
            } else {
                application.getString(R.string.read)
            }
        }
}

private const val itemOpenThresholdForAppReview = 20
const val itemDetailSourceCreator = "item_detail/creator"

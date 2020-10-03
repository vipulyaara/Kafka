package com.kafka.content.ui.detail

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.InvokeStatus
import com.data.base.extensions.debug
import com.data.base.launchObserve
import com.data.base.model.ArchiveQuery
import com.data.base.model.booksByAuthor
import com.kafka.content.domain.detail.ObserveItemDetail
import com.kafka.content.domain.detail.UpdateItemDetail
import com.kafka.content.domain.download.StartFileDownload
import com.kafka.content.domain.followed.ObserveItemFollowStatus
import com.kafka.content.domain.followed.UpdateFollowedItems
import com.kafka.content.domain.item.ObserveItems
import com.kafka.content.domain.item.UpdateItems
import com.kafka.content.domain.recent.AddRecentItem
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.ObservableErrorCounter
import com.kafka.data.model.ObservableLoadingCounter
import com.kafka.data.model.collectFrom
import com.kafka.data.model.collectInto
import com.kafka.ui_common.action.RealActioner
import com.kafka.ui_common.base.BaseViewModel
import com.kafka.ui_common.base.ReduxViewModel
import com.kafka.ui_common.base.SnackbarManager
import com.kafka.ui_common.extensions.showToast
import com.kafka.ui_common.navigation.DeepLinksNavigations
import com.kafka.ui_common.navigation.Navigation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Implementation of [BaseViewModel] to provide data for item detail.
 */
class ItemDetailViewModel @ViewModelInject constructor(
    private val updateItemDetail: UpdateItemDetail,
    private val observeItemDetail: ObserveItemDetail,
    private val observeItems: ObserveItems,
    private val updateItems: UpdateItems,
    private val startFileDownload: StartFileDownload,
    private val observeItemFollowStatus: ObserveItemFollowStatus,
    private val updateFollowedItems: UpdateFollowedItems,
    private val addRecentItem: AddRecentItem,
    private val errorState: ObservableErrorCounter,
    private val snackbarManager: SnackbarManager
) : ReduxViewModel<ItemDetailViewState>(ItemDetailViewState()) {
    private val loadingState = ObservableLoadingCounter()
    private val downloadLoadingStatus = ObservableLoadingCounter()
    private val pendingActions = RealActioner<ItemDetailAction>()

    init {
        viewModelScope.launchObserve(observeItemDetail) { flow ->
            flow.distinctUntilChanged().collectAndSetState {
                debug { "item detail fetched $it" }
                it?.let {
                    observeByAuthor(it)
                    copy(itemDetail = it)
                } ?: this
            }
        }

        viewModelScope.launch {
            loadingState.observable.collectAndSetState {
                debug { "is loading $it ${currentState().itemDetail}" }
                copy(isLoading = it)
            }
        }

        viewModelScope.launch {
            errorState.observable.distinctUntilChanged().collect {
                it?.let { snackbarManager.sendError(it) }
            }
        }

        viewModelScope.launchObserve(observeItems) { flow ->
            flow.distinctUntilChanged().collectAndSetState { list ->
                copy(itemsByCreator = list.filterNot { it.itemId == itemDetail?.itemId })
            }
        }

        viewModelScope.launchObserve(observeItemFollowStatus) { flow ->
            flow.distinctUntilChanged().collectAndSetState { copy(isFavorite = it) }
        }

        snackbarManager.launchInScope(viewModelScope) { uiError, visible ->
            viewModelScope.launchSetState {
                copy(error = if (visible) uiError.message else null)
            }
        }

        viewModelScope.launch {
            pendingActions.observe {
                when (it) {
                    is ItemDetailAction.FavoriteClick -> {
                        updateFollowedItems(UpdateFollowedItems.Params(currentState().itemDetail!!.itemId))
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun showFavoriteToast(context: Context) {
        val message = if (!currentState().isFavorite) "Added to favorites" else "Removed from favorites"
        context.showToast(message)
    }

    private fun observeByAuthor(itemDetail: ItemDetail?) {
        itemDetail?.let {
            debug { "observe query for creator ${itemDetail.creator}" }

            observeItemFollowStatus(ObserveItemFollowStatus.Params(itemDetail.itemId))
            itemDetail.creator?.let { ArchiveQuery().booksByAuthor(it) }?.let {
                observeItems(ObserveItems.Params(it))
                updateItems(UpdateItems.Params(it))
            }
        }
    }

    fun setInitialData(itemDetail: ItemDetail) {
        viewModelScope.withState {
            if (it.itemDetail == null) {
                viewModelScope.launchSetState {
                    copy(itemDetail = itemDetail)
                }
            }
        }
    }

    fun observeItemDetail(contentId: String) {
        debug { "observe item detail for $contentId" }
        observeItemDetail(ObserveItemDetail.Param(contentId))
    }

    fun updateItemDetail(contentId: String) {
        debug { "update item detail for $contentId" }
        updateItemDetail(UpdateItemDetail.Param(contentId))
            .also { viewModelScope.launch { it.collectInto(loadingState) } }
            .also { viewModelScope.launch { errorState.collectFrom(it) } }
    }

    fun addRecentItem() {
        addRecentItem(AddRecentItem.Params(currentState().itemDetail?.itemId!!))
    }

    fun sendAction(action: ItemDetailAction) {
        viewModelScope.launch { pendingActions.sendAction(action) }
    }


    private fun Flow<InvokeStatus>.watchStatus(loadingState: ObservableLoadingCounter) =
        viewModelScope.launch { collectStatus(loadingState) }

    private suspend fun Flow<InvokeStatus>.collectStatus(loadingState: ObservableLoadingCounter) = collect { status ->
        when (status) {
            InvokeStatus.Loading -> {
                loadingState.addLoader()
                debug { "is status loading $status" }
            }
            InvokeStatus.Success -> {
                loadingState.removeLoader()
            }
            is InvokeStatus.Error -> {
                loadingState.removeLoader()
                debug { "is status error $status" }
            }
        }
    }

    fun shareItemText(itemId: String): String {
        val itemTitle = currentState().itemDetail?.title

        return """
            $itemTitle
            I really liked this book on the Kafka app. I think you will enjoy it.
            
            ${DeepLinksNavigations.findUri(Navigation.ItemDetail(itemId))}
        """.trimIndent()
    }

    fun download(context: Context, readerUrl: String, title: String) {
        debug { "downloading pdf with url $readerUrl" }

        addRecentItem()

        startFileDownload(readerUrl).watchStatus(downloadLoadingStatus)
    }

    fun read(context: Context, readerUrl: String, title: String) {
        debug { "opening pdf with url $readerUrl" }

        addRecentItem()

//        val config = ViewerConfig.Builder()
//            .openUrlCachePath(context.cacheDir.absolutePath)
//            .fullscreenModeEnabled(true)
//            .multiTabEnabled(false)
//            .documentEditingEnabled(false)
//            .longPressQuickMenuEnabled(true)
//            .toolbarTitle(title)
//            .showSearchView(true)
//            .build()
//        DocumentActivity.openDocument(context, readerUrl.toUri(), config)
    }
}

package com.kafka.library.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.data.entities.BookshelfDefaults
import com.kafka.data.entities.BookshelfItem
import com.kafka.domain.interactors.library.UploadBook
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.domain.observers.library.ObserveBookshelfItems
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import com.kafka.networking.localizedMessage
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class BookshelfDetailViewModel(
    @Assisted private val bookshelfId: String,
    observeBookshelfItems: ObserveBookshelfItems,
    observeUser: ObserveUser,
    private val snackbarManager: SnackbarManager,
    private val uploadBook: UploadBook,
    private val analytics: Analytics,
    private val navigator: Navigator
) : ViewModel() {
    val state = combine(
        observeUser.flow,
        observeBookshelfItems.flow,
        uploadBook.inProgress
    ) { user, items, loading ->
        BookshelfDetailState(items = items, isUserLoggedIn = user != null, loading = loading)
    }.stateInDefault(viewModelScope, BookshelfDetailState())

    val isUploads = bookshelfId == BookshelfDefaults.uploads.id

    init {
        observeBookshelfItems(ObserveBookshelfItems.Params(bookshelfId))
        observeUser(ObserveUser.Params())
    }

    fun openItemDetail(itemId: String) {
        analytics.log { openItemDetail(itemId = itemId, source = bookshelfId) }

        if (isUploads) {
            navigator.navigate(Screen.EpubReader(itemId, itemId))
        } else {
            navigator.navigate(Screen.ItemDetail(itemId))
        }
    }

    fun chooseFile() {
        viewModelScope.launch {
            uploadBook(Unit).onException {
                snackbarManager.addMessage(it.localizedMessage())
            }
        }
    }

    fun openLogin() {
        navigator.navigate(Screen.Login)
    }
}

data class BookshelfDetailState(
    val items: List<BookshelfItem> = listOf(),
    val isUserLoggedIn: Boolean = false,
    val loading: Boolean = false
)

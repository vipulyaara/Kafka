package com.kafka.library.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.BookshelfItem
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.domain.observers.library.ObserveBookshelfItems
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.flow.combine
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class BookshelfDetailViewModel(
    @Assisted private val bookshelfId: String,
    observeBookshelfItems: ObserveBookshelfItems,
    observeUser: ObserveUser,
    private val analytics: Analytics,
    private val navigator: Navigator
) : ViewModel() {
    val state = combine(
        observeUser.flow,
        observeBookshelfItems.flow
    ) { user, items ->
        BookshelfDetailState(items = items, isUserLoggedIn = user != null)
    }.stateInDefault(viewModelScope, BookshelfDetailState())

    init {
        observeBookshelfItems(ObserveBookshelfItems.Params(bookshelfId))
        observeUser(ObserveUser.Params())
    }

    fun openItemDetail(itemId: String) {
        analytics.log { this.openItemDetail(itemId = itemId, source = bookshelfId) }
        navigator.navigate(Screen.ItemDetail(itemId))
    }

    fun openLogin() {
        navigator.navigate(Screen.Login)
    }
}

data class BookshelfDetailState(
    val items: List<BookshelfItem> = listOf(),
    val isUserLoggedIn: Boolean = false,
)

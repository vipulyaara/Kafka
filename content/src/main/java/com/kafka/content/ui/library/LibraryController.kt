package com.kafka.content.ui.library

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.content.ui.search.SearchViewState
import com.kafka.data.entities.Item
import com.kafka.ui_common.ui.gridCarousel
import com.kafka.ui_common.ui.withModelsFrom
import kotlinx.coroutines.channels.Channel

class LibraryController : TypedEpoxyController<LibraryViewState>() {
    lateinit var actioner: Channel<LibraryAction>

    override fun buildModels(data: LibraryViewState?) {
        data?.apply {
            favorites?.let { favorites(it) }
        }
    }

    private fun empty() {
        emptyState {
            id("empty_state")
            title("No Favorites")
            text("Add books to favorites to see them here")
        }
    }

    private fun loading(searchViewState: SearchViewState) {
        if (searchViewState.isLoading && searchViewState.items == null) {
            loader { id("loading") }
        }
    }
    private fun favorites(favorites: List<Item>) {
        verticalSpacingMedium { id("favorites_spacing_top") }
        fancySectionHeader {
            id("favorites_header")
            text("Favorites")
        }
        gridCarousel {
            id("favorites")
            padding(Carousel.Padding.dp(12, 12, 12, 12, 2))
            withModelsFrom(favorites.map { it }) {
                BookGridBindingModel_().apply {
                    id(it.itemId)
                    clickListener { _ ->  actioner.sendAction(LibraryAction.ItemDetailAction(it)) }
                    item(it)
                }
            }
        }
    }
}

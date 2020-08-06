package com.kafka.content.ui.homepage

import android.widget.EditText
import coil.api.clear
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.content.databinding.ItemBookBinding
import com.kafka.content.ui.ActionListener
import com.kafka.content.ui.search.SearchAction
import com.kafka.content.ui.search.SearchQuery
import com.kafka.content.ui.search.SearchViewState
import com.kafka.data.entities.Item
import com.kafka.data.extensions.letEmpty
import com.kafka.ui_common.ui.kafkaCarousel
import com.kafka.ui_common.ui.withModelsFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomepageController @Inject constructor() : TypedEpoxyController<HomepageViewState>() {

    lateinit var homepageActioner: Channel<HomepageAction>
    lateinit var searchActioner: Channel<SearchAction>

    override fun buildModels(data: HomepageViewState?) {
        data?.apply {
//            search()
            banner()
            favorites.letEmpty { continueReading(it) }
            tags(tags)
            searchViewState.items?.letEmpty { data.favorites?.let { it1 -> searchResults(it, it1) } }
            loading(searchViewState)
//            empty(searchViewState)
        }
    }

    private fun empty(searchViewState: SearchViewState) {
        if (!searchViewState.isLoading && searchViewState.items.isNullOrEmpty()) {
            emptyState {
                id("empty")
                text("No results found")
            }
        }
    }

    private fun loading(searchViewState: SearchViewState) {
        if (searchViewState.isLoading && searchViewState.items.isNullOrEmpty()) {
            loader { id("loading") }
        }
    }

    private fun search() {
        searchView {
            id("search")
            actionListener(object : ActionListener {
                override fun onAction(editText: EditText) {
                    sendAction(SearchAction.SubmitQueryAction(SearchQuery(editText.text.toString())))
                }
            })
        }
    }

    private fun banner() {
        banner {
            id("banner")
        }
    }

    private fun searchResults(it: List<Item>, favorites: List<Item>) {
        it.forEachIndexed { index, item ->
            if (index == 5) {
                favorites.letEmpty { favorites(it) }
            }

            book {
                onUnbind { _, view ->
                    (view.dataBinding as ItemBookBinding).heroImage.clear()
                }
                id(item.itemId)
                item(item)
                clickListener { _ -> sendAction(SearchAction.ItemDetailAction(item)) }
            }
        }
    }

    private fun tags(it: List<HomepageTag>) {
        kafkaCarousel {
            onBind { model, view, position ->
                view.scrollToPosition(0)
            }
            id("recent_search")
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(it) {
                HomepageTagBindingModel_().apply {
                    id(it.title)
                    text(it.title)
                    isSelected(it.isSelected)
                    clickListener { _ -> sendAction(HomepageAction.SelectTag(HomepageTag(it.title))) }
                }
            }
        }
    }

    private fun continueReading(favorites: List<Item>) {
        verticalSpacingMedium { id("continue_reading_spacing_top") }
        sectionHeader {
            id("continue_reading_header")
            text("Continue Reading")
        }
        kafkaCarousel {
            id("continue_reading")
            padding(Carousel.Padding.dp(12, 12, 12, 12, 2))
            numViewsToShowOnScreen(1.2f)
            hasFixedSize(true)
            withModelsFrom(favorites.map { it }) {
                BookOnShelfBindingModel_().apply {
                    id(it.itemId)
                    item(it)
                    clickListener { _ -> sendAction(SearchAction.ItemDetailAction(it)) }
                }
            }
        }
    }

    private fun favorites(favorites: List<Item>) {
        verticalSpacingMedium { id("favorites_spacing_top") }
        fancySectionHeader {
            id("favorites_header")
            text("Favorites")
        }
        kafkaCarousel {
            id("favorites")
            padding(Carousel.Padding.dp(12, 12, 12, 12, 2))
            numViewsToShowOnScreen(1.9f)
            hasFixedSize(true)
            withModelsFrom(favorites.map { it }) {
                BookGridBindingModel_().apply {
                    id(it.itemId)
                    item(it)
                    clickListener { _ -> sendAction(SearchAction.ItemDetailAction(it)) }
                }
            }
        }
    }

    private fun sendAction(homepageAction: HomepageAction) = GlobalScope.launch {
        homepageActioner.send(homepageAction)
    }

    private fun sendAction(searchAction: SearchAction) = GlobalScope.launch {
        searchActioner.send(searchAction)
    }
}

package com.kafka.content.ui.homepage

import coil.api.clear
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.content.databinding.ItemBookBinding
import com.kafka.content.ui.search.SearchAction
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
            banner()
            favorites.letEmpty { continueReading(it) }
            tags(tags)
            searchViewState.items?.letEmpty { data.favorites?.let { it1 -> searchResults(it, it1) } }
            loading(searchViewState)
            empty(searchViewState)
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

    private fun banner() {
        kafkaCarousel {
            id("banner")
            padding(Carousel.Padding.dp(0, 4))
            numViewsToShowOnScreen(1.1f)
            withModelsFrom(bannerImages) {
                BannerBindingModel_().apply {
                    id("$it banner")
                    resource(it)
                }
            }
        }
    }

    private fun searchResults(it: List<Item>, favorites: List<Item>) {
        it.forEachIndexed { index, item ->
            if (index == 5) {
                favorites.letEmpty { favorite(it) }
            }

            book {
                onUnbind { _, view ->
                    (view.dataBinding as ItemBookBinding).heroImage.clear()
                }
                id(item.itemId)
                item(item)
                clickListener { view -> sendAction(SearchAction.ItemDetailWithSharedElement(item, view)) }
            }
        }
    }

    private fun tags(it: List<HomepageTag>) {
        kafkaCarousel {
            onBind { _, view, _ ->
                val position = it.indexOfFirst { it.isSelected }
                view.scrollToPosition(position)
            }
            id("recent_search")
            padding(Carousel.Padding.dp(12, 0, 12, 0, 24))
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
            padding(Carousel.Padding.dp(12, 8, 12, 12, 2))
            numViewsToShowOnScreen(1.3f)
            hasFixedSize(true)
            withModelsFrom(favorites.map { it }) {
                BookOnShelfBindingModel_().apply {
                    id(it.itemId)
                    item(it)
                    clickListener { view -> sendAction(SearchAction.ItemDetailWithSharedElement(it, view)) }
                }
            }
        }
    }

    private fun favorite(favorites: List<Item>) {
        sectionHeader {
            id("favorite_header")
            text("Favorites")
        }
        kafkaCarousel {
            id("favorite")
            padding(Carousel.Padding.dp(12, 0, 12, 12, 2))
            numViewsToShowOnScreen(1.3f)
            hasFixedSize(true)
            withModelsFrom(favorites.map { it }) {
                BookOnShelfBindingModel_().apply {
                    id(it.itemId)
                    item(it)
                    clickListener { view -> sendAction(SearchAction.ItemDetailWithSharedElement(it, view)) }
                }
            }
        }
    }

    fun setSearchViewState(searchViewState: SearchViewState) {
        setData(
            currentData?.copy(searchViewState = searchViewState)
                ?: HomepageViewState(searchViewState = searchViewState)
        )
    }

    fun setHomepageViewState(homepageViewState: HomepageViewState) {
        setData(
            currentData?.copy(
                favorites = homepageViewState.favorites,
                recentItems = homepageViewState.recentItems,
                tags = homepageViewState.tags
            )
        )
    }

    private fun sendAction(homepageAction: HomepageAction) = GlobalScope.launch {
        homepageActioner.send(homepageAction)
    }

    private fun sendAction(searchAction: SearchAction) = GlobalScope.launch {
        searchActioner.send(searchAction)
    }
}

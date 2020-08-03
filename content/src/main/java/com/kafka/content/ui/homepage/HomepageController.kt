package com.kafka.content.ui.homepage

import android.content.Context
import android.widget.EditText
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.content.ui.ActionListener
import com.kafka.content.ui.search.SearchAction
import com.kafka.content.ui.search.SearchQuery
import com.kafka.content.ui.search.SearchViewState
import com.kafka.data.entities.Item
import com.kafka.data.extensions.letEmpty
import com.kafka.ui_common.ui.kafkaCarousel
import com.kafka.ui_common.ui.withModelsFrom
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomepageController @Inject constructor(
    @ActivityContext private val context: Context
) : TypedEpoxyController<HomepageViewState>() {

    lateinit var homepageActioner: Channel<HomepageAction>
    lateinit var searchActioner: Channel<SearchAction>

    override fun buildModels(data: HomepageViewState?) {
        data?.apply {
            search()
            banner()
            loading(searchViewState)
            tags(tags)
            searchViewState.items?.let { data.favorites?.let { it1 -> searchResults(it, it1) } }
        }
    }

    private fun loading(searchViewState: SearchViewState) {
        if (searchViewState.isLoading && searchViewState.items == null) {
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
                id(item.itemId)
                item(item)
                clickListener { _ -> sendAction(SearchAction.ItemDetailAction(item)) }
            }
        }
    }

    private fun tags(it: List<HomepageTag>) {
        kafkaCarousel {
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

    private fun favorites(favorites: List<Item>) {
        verticalSpacingMedium { id("favorites_spacing_top") }
        fancySectionHeader {
            id("favorites_header")
            text("Favorites")
        }
        kafkaCarousel {
            id("favorites")
            padding(Carousel.Padding.dp(12, 12, 12, 12, 2))
            itemWidth(context.resources.getDimensionPixelSize(R.dimen.homepage_favorite_item_width))
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

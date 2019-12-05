package com.kafka.user.feature.search

import android.view.View
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Content
import com.kafka.user.ItemAuthorBindingModel_
import com.kafka.user.ItemBookBindingModel_
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.itemLoader
import com.kafka.user.itemRowHeader
import com.kafka.user.itemSearch

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class SearchController constructor(private val callbacks: SearchController.Callbacks) :
    BaseEpoxyController<SearchViewState>() {

    override fun buildModels(viewState: SearchViewState) {
        if (viewState.isLoading) {
            buildLoadingState()
        } else {
            buildSearchModels(viewState)
        }
    }

    private fun buildLoadingState() {
        itemLoader { id("loader") }
    }

    private fun buildSearchModels(viewState: SearchViewState) {

        itemSearch {
            id("Search")
        }

        itemRowHeader {
            id("row headers")
            text("Books by Kafka")
        }

        carousel {
            id("items by kafka")
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemBookBindingModel_()
                    .id(it)
                    .transitionName("poster")
                    .itemClickListener { v, _, clickedView, _ ->
                    }
                    .resource(getRandomAuthorResource())
            }
        }

        itemRowHeader {
            id("row header author")
            text("Popular authors")
        }

        carousel {
            id("authors")
            padding(Carousel.Padding.dp(12, 4))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemAuthorBindingModel_()
                    .id(it)
                    .resource(getRandomAuthorResource())
            }
        }
    }

    interface Callbacks {
        fun onBookClicked(view: View, content: Content)
    }
}

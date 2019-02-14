package com.airtel.kafkapp.feature.search

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.airtel.data.entities.Book
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.ItemSearchHistoryBindingModel_
import com.airtel.kafkapp.R
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.itemRowHeader
import com.airtel.kafkapp.itemSearch
import com.airtel.kafkapp.ui.epoxy.carousel
import com.airtel.kafkapp.ui.epoxy.withModelsFrom

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
class SearchController : TypedEpoxyController<Book>() {
    override fun buildModels(data: Book?) {

        itemSearch { id("search") }

        itemRowHeader {
            id("row header")
            text("Trending books")
        }

        carousel {
            id("suggestions")
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 7)) {
                ItemBookBindingModel_()
                    .id(it)
                    .resource(getRandomCoverResource())
            }
        }

        itemRowHeader {
            id("row header authors")
            text("Search history")
            resource(R.drawable.ic_delete_black_24dp)
        }

        carousel {
            id("authors")
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(arrayListOf(
                "The Ring", "Look up", "Kafka"
            )) {
                ItemSearchHistoryBindingModel_()
                    .id(it)
                    .text(it)
            }
        }
    }
}

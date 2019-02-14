package com.airtel.kafkapp.feature.home

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.airtel.data.entities.Book
import com.airtel.data.entities.Item
import com.airtel.kafkapp.ItemAuthorBindingModel_
import com.airtel.kafkapp.ItemBannerBindingModel_
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.extensions.getBooks
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.itemRowHeader
import com.airtel.kafkapp.ui.epoxy.carousel
import com.airtel.kafkapp.ui.epoxy.withModelsFrom

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
class HomeController(
    private val callbacks: Callbacks
) : TypedEpoxyController<List<Item>>() {
    override fun buildModels(data: List<Item>?) {

        carousel {
            id("banner")
            padding(Carousel.Padding.dp(2, 2))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemBannerBindingModel_()
                    .id(it)
            }
        }

        data?.let {

            itemRowHeader {
                id("row headers")
                text("Books by Kafka")
            }

            carousel {
                id("books by kafka")
                padding(Carousel.Padding.dp(12, 12))
                withModelsFrom(data) {
                    ItemBookBindingModel_()
                        .id(it.id)
                        .transitionName("poster")
                        .itemClickListener { v, _, clickedView, _ ->
                            callbacks.onBookClicked(clickedView, it)
                        }
                        .resource(getRandomCoverResource())
                }
            }
        }

        itemRowHeader {
            id("row header")
            text("Featured")
        }

        carousel {
            id("suggestions")
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(getBooks()) {
                ItemBookBindingModel_()
                    .id(it.id)
                    .transitionName("poster")
                    .itemClickListener { v, _, clickedView, _ ->
                        callbacks.onBookClicked(clickedView, Item())
                    }
                    .resource(getRandomCoverResource())
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
                    .resource(getRandomCoverResource())
            }
        }
    }

    interface Callbacks {
        fun onBookClicked(viewHolderId: View, item: Item)
    }
}

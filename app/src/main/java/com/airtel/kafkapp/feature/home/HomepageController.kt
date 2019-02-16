package com.airtel.kafkapp.feature.home

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airtel.data.entities.Item
import com.airtel.kafkapp.ItemAuthorBindingModel_
import com.airtel.kafkapp.ItemBannerBindingModel_
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.extensions.carousel
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.extensions.withModelsFrom
import com.airtel.kafkapp.feature.common.BaseEpoxyController
import com.airtel.kafkapp.itemLoader
import com.airtel.kafkapp.itemRowHeader

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class HomepageController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<HomepageViewState>() {

    override fun buildModels(viewState: HomepageViewState) {
        if (viewState.isLoading) {
            buildLoadingState()
        } else {
            buildHomepageModels(viewState)
        }
    }

    private fun buildLoadingState() {
        itemLoader { id("loader") }
    }

    private fun buildHomepageModels(viewState: HomepageViewState) {

        carousel {
            id("banner")
            padding(Carousel.Padding.dp(2, 2))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemBannerBindingModel_()
                    .id(it)
            }
        }

        viewState.items?.let { items ->
            itemRowHeader {
                id("row headers")
                text("Books by Kafka")
            }

            carousel {
                id("items by kafka")
                padding(Carousel.Padding.dp(12, 12))
                withModelsFrom(items) {
                    ItemBookBindingModel_()
                        .id(it.itemId)
                        .item(it)
                        .transitionName("poster")
                        .itemClickListener { v, _, clickedView, _ ->
                            callbacks.onBookClicked(clickedView, it)
                        }
                }
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

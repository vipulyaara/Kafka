package com.airtel.kafkapp.feature.home

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.Carousel
import com.airtel.data.entities.Item
import com.airtel.kafkapp.ItemAuthorBindingModel_
import com.airtel.kafkapp.ItemBannerBindingModel_
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.extensions.carousel
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.extensions.logger
import com.airtel.kafkapp.extensions.withModelsFrom
import com.airtel.kafkapp.feature.common.BaseEpoxyController
import com.airtel.kafkapp.itemLoader
import com.airtel.kafkapp.itemPageHeader
import com.airtel.kafkapp.itemRowHeader

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class HomepageController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<HomepageViewState>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun buildHomepageModels(viewState: HomepageViewState) {
        itemPageHeader {
            id("headers")
        }

        carousel {
            id("banner")
            padding(Carousel.Padding.dp(0, 0))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemBannerBindingModel_()
                    .itemClickListener { _, _, _, _ ->
                        callbacks.onBannerClicked()
                    }
                    .id(it)
            }
        }

        viewState.items?.forEach {
            if (it.items?.isNotEmpty() == true) {
                itemRowHeader {
                    id("header ${it.title}")
                    logger.d("ID ${it.title + it.items.hashCode()}")
                    text(it.title)
                }

                carousel {
                    id(it.title)
                    padding(Carousel.Padding.dp(12, 12))
                    withModelsFrom(it.items ?: arrayListOf()) {
                        ItemBookBindingModel_()
                            .id(it.itemId)
                            .item(it)
                            .itemClickListener { _, _, clickedView, _ ->
                                clickedView.transitionName = it.itemId
                                callbacks.onBookClicked(clickedView, it)
                            }
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
        fun onBookClicked(view: View, item: Item)
        fun onBannerClicked()
    }
}

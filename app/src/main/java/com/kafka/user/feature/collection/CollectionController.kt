package com.kafka.user.feature.collection

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Item
import com.kafka.user.ItemBookAltBindingModel_
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.itemCollectionHeader
import com.kafka.user.itemLoader
import com.kafka.user.ui.epoxy.gridContentCarousel

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class CollectionController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<CollectionViewState>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun buildModels(viewState: CollectionViewState) {
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
    private fun buildHomepageModels(viewState: CollectionViewState) {
        itemCollectionHeader { id("collection") }

        viewState.items?.let {
            gridContentCarousel {
                id("authors")
                padding(Carousel.Padding.dp(8,16,8, 24, 8))
                withModelsFrom(it) {
                    ItemBookAltBindingModel_()
                        .id(it.contentId)
                        .item(it)
                        .resource(getRandomAuthorResource())
                }
            }
        }
    }

    interface Callbacks {
        fun onBookClicked(view: View, item: Item)
        fun onBannerClicked()
    }
}

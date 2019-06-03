package com.kafka.user.feature.profile

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Item
import com.kafka.user.ItemBookAltBindingModel_
import com.kafka.user.ItemBookBindingModel_
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.itemCollectionDetail
import com.kafka.user.itemLoader
import com.kafka.user.itemRowHeader
import com.kafka.user.ui.epoxy.gridContentCarousel

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class ProfileController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<ProfileViewState>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun buildModels(viewState: ProfileViewState) {
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
    private fun buildHomepageModels(viewState: ProfileViewState) {
        itemCollectionDetail { id("collection") }

        viewState.items?.let {
            gridContentCarousel {
                id("authors")
                padding(Carousel.Padding.dp(8, 8))
                withModelsFrom(it) {
                    ItemBookAltBindingModel_()
                        .id(it.itemId)
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

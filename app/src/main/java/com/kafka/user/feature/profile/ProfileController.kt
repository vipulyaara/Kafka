package com.kafka.user.feature.profile

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Item
import com.kafka.user.ItemAuthorBindingModel_
import com.kafka.user.ItemBannerBindingModel_
import com.kafka.user.extensions.carousel
import com.kafka.user.extensions.getRandomCoverResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.itemLoader
import com.kafka.user.itemRowHeader

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

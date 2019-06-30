package com.kafka.user.feature.language

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Item
import com.kafka.user.ItemAuthorBindingModel_
import com.kafka.user.ItemBannerBindingModel_
import com.kafka.user.ItemBookAltBindingModel_
import com.kafka.user.ItemBookBindingModel_
import com.kafka.user.extensions.carousel
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.feature.home.HomepageViewState
import com.kafka.user.itemFooter
import com.kafka.user.itemLoader
import com.kafka.user.itemPageHeader
import com.kafka.user.itemRowHeader
import com.kafka.user.ui.epoxy.contentCarousel

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class LanguageController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<LanguageViewState>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun buildModels(viewState: LanguageViewState) {
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
    private fun buildHomepageModels(viewState: LanguageViewState) {

        carousel {
            id("banner")
            padding(Carousel.Padding.dp(0, 0))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemBannerBindingModel_()
                    .itemClickListener { _, _, _, _ ->
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
                    .resource(getRandomAuthorResource())
            }
        }

        itemFooter { id("footer") }
    }

    interface Callbacks {
    }
}

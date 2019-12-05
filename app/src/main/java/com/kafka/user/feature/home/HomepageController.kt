package com.kafka.user.feature.home

import android.view.View
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Content
import com.kafka.data.extensions.observable
import com.kafka.user.ItemAuthorBindingModel_
import com.kafka.user.ItemBannerBindingModel_
import com.kafka.user.ItemBookAltBindingModel_
import com.kafka.user.ItemBookBindingModel_
import com.kafka.user.extensions.carousel
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.itemFooter
import com.kafka.user.itemLoader
import com.kafka.user.itemPageHeader
import com.kafka.user.itemRowHeader
import com.kafka.user.ui.epoxy.contentCarousel
import org.jsoup.internal.StringUtil.padding

/**
 * @author Vipul Kumar; dated 19/01/19.
 *
 * Controller to layout items on homepage.
 */

class HomepageController constructor() : BaseEpoxyController() {

    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state: HomepageViewState by observable(HomepageViewState(), ::requestModelBuild)

    override fun buildModels() {
        if (state.isLoading) {
            buildLoadingState()
        } else {
            buildHomepageModels(state)
        }
    }

    private fun buildLoadingState() {
        itemLoader { id("loader") }
    }

    private fun buildHomepageModels(viewState: HomepageViewState) {

        viewState.items?.forEach { railItem ->
            if (railItem.contents?.isNotEmpty() == true) {
                itemRowHeader {
                    id("header ${railItem.title}")
                    text(railItem.title.substring(2, railItem.title.length))
                }

                contentCarousel {
                    id(railItem.title)
                    padding(Carousel.Padding.dp(16, 12,16,32,16))
                    withModelsFrom(railItem.contents ?: arrayListOf()) {
                        ItemBookBindingModel_()
                            .id(it.contentId)
                            .item(it)
                            .resource(getRandomAuthorResource())
                            .itemClickListener { _, _, clickedView, _ ->
                                clickedView.transitionName = it.contentId
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
                    .resource(getRandomAuthorResource())
            }
        }

        itemFooter { id("footer") }
    }

    interface Callbacks {
        fun onBookClicked(view: View, content: Content)
        fun onBannerClicked()
    }
}

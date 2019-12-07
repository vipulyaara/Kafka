package com.kafka.user.feature.home

import android.view.View
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Content
import com.kafka.data.extensions.observable
import com.kafka.user.*
import com.kafka.user.extensions.carousel
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.ui.epoxy.contentCarousel
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 19/01/19.
 *
 * Controller to layout items on homepage.
 */

class HomepageController @Inject constructor() : BaseEpoxyController() {

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
        loader { id("loader") }
    }

    private fun buildHomepageModels(viewState: HomepageViewState) {

        viewState.items?.forEach { railItem ->
            if (railItem.contents?.isNotEmpty() == true) {
                rowHeader {
                    id("header ${railItem.title}")
                    text(railItem.title.substring(2, railItem.title.length))
                }

                contentCarousel {
                    id(railItem.title)
                    padding(Carousel.Padding.dp(16, 12,16,32,16))
                    withModelsFrom(railItem.contents ?: arrayListOf()) {
                        BookBindingModel_()
                            .id(it.contentId)
                            .content(it)
                            .resource(getRandomAuthorResource())
                            .itemClickListener { _, _, clickedView, _ ->
                                clickedView.transitionName = it.contentId
                                callbacks?.onContentClicked(clickedView, it)
                            }
                    }
                }
            }
        }

        rowHeader {
            id("row header author")
            text("Popular authors")
        }

        carousel {
            id("authors")
            padding(Carousel.Padding.dp(12, 4))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                AuthorBindingModel_()
                    .id(it)
                    .resource(getRandomAuthorResource())
            }
        }

        footer { id("footer") }
    }

    interface Callbacks {
        fun onContentClicked(view: View, content: Content)
        fun onBannerClicked()
    }
}

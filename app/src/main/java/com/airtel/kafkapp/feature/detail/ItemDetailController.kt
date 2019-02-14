package com.airtel.kafkapp.feature.detail

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.itemBookDetail
import com.airtel.kafkapp.itemRowHeader
import com.airtel.kafkapp.ui.epoxy.carousel
import com.airtel.kafkapp.ui.epoxy.withModelsFrom

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
class ItemDetailController(
    private val callbacks: Callbacks
) : TypedEpoxyController<ItemDetailViewState>() {
    override fun buildModels(viewState: ItemDetailViewState?) {

        viewState?.itemDetail?.let {
            itemBookDetail {
                id(it.id)
                item(it)
                clickListener { _, _, clickedView, _ ->
                    clickedView.animateBookOpen()
                }
                reviewsClickListener { _, _, _, _ ->
                    callbacks.onReviewsClicked()
                }
            }
        }

        viewState?.itemsByCreator?.let { list ->
            itemRowHeader {
                id("row header")
                text("Books by Franz Kafka")
            }

            carousel {
                id("suggestions")
                padding(Carousel.Padding.dp(12, 12))
                withModelsFrom(list) {
                    ItemBookBindingModel_()
                        .id(it.contentId)
                        .item(it)
                        .resource(getRandomCoverResource())
                }
            }
        }
    }

    private fun View.animateBookOpen() {
        this.animate()
            .scaleX(2.5f)
            .scaleY(2.5f)
            .rotationY(-80f)
            .translationXBy(-100f)
            .setDuration(700)
            .start()
    }

    interface Callbacks {
        fun onReviewsClicked()
    }
}

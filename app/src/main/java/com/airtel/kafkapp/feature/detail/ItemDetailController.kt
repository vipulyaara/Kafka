package com.airtel.kafkapp.feature.detail

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airtel.data.entities.Item
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.extensions.carousel
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.extensions.withModelsFrom
import com.airtel.kafkapp.feature.common.BaseEpoxyController
import com.airtel.kafkapp.itemBookDetail
import com.airtel.kafkapp.itemLoader
import com.airtel.kafkapp.itemRowHeader

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class ItemDetailController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<ItemDetailViewState>() {

    override fun buildModels(viewState: ItemDetailViewState) {
        if (viewState.isLoading) {
            buildLoadingState()
        } else {
            buildDetailModels(viewState)
        }
    }

    private fun buildLoadingState() {
        itemLoader { id("loader") }
    }

    private fun buildDetailModels(viewState: ItemDetailViewState) {

        itemBookDetail {
            id(viewState.itemDetail?.itemId)
            item(viewState.itemDetail)
            clickListener { _, _, clickedView, _ ->
                clickedView.animateBookOpen()
            }
            reviewsClickListener { _, _, _, _ ->
                callbacks.onReviewsClicked()
            }
        }

        viewState.itemsByCreator?.let { list ->
            itemRowHeader {
                id("row header")
                text("Books by Franz Kafka")
            }

            carousel {
                id("suggestions")
                padding(Carousel.Padding.dp(12, 12))
                withModelsFrom(list) {
                    ItemBookBindingModel_()
                        .id(it.itemId)
                        .item(it)
                        .itemClickListener { model, parentView, clickedView, position ->
                            callbacks.onItemClicked(it)
                        }
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
        fun onItemClicked(item: Item)
    }
}

package com.airtel.kafkapp.feature.detail

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airtel.data.entities.Item
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.databinding.ItemBookDetailBinding
import com.airtel.kafkapp.extensions.carousel
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.extensions.withModelsFrom
import com.airtel.kafkapp.feature.common.BaseEpoxyController
import com.airtel.kafkapp.itemBookDetail
import com.airtel.kafkapp.itemLoader
import com.airtel.kafkapp.itemRowHeader
import com.airtel.kafkapp.ui.SharedElementHelper

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class ItemDetailController constructor(private val callbacks: Callbacks) :
    BaseEpoxyController<ItemDetailViewState>() {

    override fun buildModels(viewState: ItemDetailViewState) {
        if (viewState.isLoading && viewState.itemDetail == null) {
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
            clickListener { _, parentView, clickedView, _ ->
                clickedView.animateBookOpen()
                (parentView.dataBinding as ItemBookDetailBinding).coverCard2.animateScale()
            }
            reviewsClickListener { _, _, _, _ ->
                callbacks.onReviewsClicked()
            }
        }

        viewState.itemsByCreator?.let { list ->
            itemRowHeader {
                id("row header")
                text(list.title)
            }

            carousel {
                id("suggestions")
                padding(Carousel.Padding.dp(12, 12))
                withModelsFrom(list.items ?: arrayListOf()) {
                    ItemBookBindingModel_()
                        .id(it.itemId)
                        .item(it)
                        .itemClickListener { model, parentView, clickedView, position ->
                            callbacks.onItemClicked(it, SharedElementHelper().apply {
                                addSharedElement(clickedView, "poster")
                            })
                        }
                        .resource(getRandomCoverResource())
                }
            }
        }
    }

    private fun View.animateBookOpen() {
        this.animate()
            .scaleX(2f)
            .scaleY(2f)
            .rotationY(-90f)
            .translationX(-500f)
            .setDuration(500)
            .start()
    }

    private fun View.animateScale() {
        this.animate()
            .scaleX(4f)
            .scaleY(4f)
            .translationX(-500f)
            .setDuration(500)
            .start()
    }

    interface Callbacks {
        fun onReviewsClicked()
        fun onItemClicked(item: Item, sharedElements: SharedElementHelper)
    }
}

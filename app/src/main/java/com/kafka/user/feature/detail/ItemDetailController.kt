package com.kafka.user.feature.detail

import android.view.View
import com.airbnb.epoxy.Carousel
import com.kafka.data.entities.Item
import com.kafka.user.ItemBookBindingModel_
import com.kafka.user.databinding.ItemBookDetailBinding
import com.kafka.user.extensions.carousel
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.itemBookDetail
import com.kafka.user.itemLoader
import com.kafka.user.itemRowHeader
import com.kafka.user.ui.SharedElementHelper

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
            resource(getRandomAuthorResource())
            clickListener { _, parentView, clickedView, _ ->
                clickedView.animateBookOpen()
                (parentView.dataBinding as ItemBookDetailBinding).coverCard2.animateScale()
            }
            reviewsClickListener { _, _, _, _ ->
                callbacks.onReviewClicked()
            }
            downloadClickListener { _, _, _, _ ->
                callbacks.onDownloadClicked()
            }
            profileClickListener { _, _, _, _ ->
                callbacks.onProfileClicked()
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
                        .itemClickListener { _, _, clickedView, _ ->
                            callbacks.onItemClicked(it, SharedElementHelper().apply {
                                addSharedElement(clickedView, "poster")
                            })
                        }
                        .resource(getRandomAuthorResource())
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
        fun onDownloadClicked()
        fun onReviewClicked()
        fun onProfileClicked()
        fun onItemClicked(item: Item, sharedElements: SharedElementHelper)
    }
}

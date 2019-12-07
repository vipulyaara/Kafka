package com.kafka.user.feature.detail

import android.view.View
import com.airbnb.epoxy.Carousel
import com.kafka.data.data.config.logging.Logger
import com.kafka.data.entities.Content
import com.kafka.data.entities.ContentDetail
import com.kafka.data.extensions.observable
import com.kafka.user.BookBindingModel_
import com.kafka.user.bookDetail
import com.kafka.user.databinding.ItemBookDetailBinding
import com.kafka.user.extensions.carousel
import com.kafka.user.extensions.getRandomAuthorResource
import com.kafka.user.extensions.withModelsFrom
import com.kafka.user.feature.common.BaseEpoxyController
import com.kafka.user.loader
import com.kafka.user.rowHeader
import com.kafka.user.ui.SharedElementHelper
import org.jsoup.internal.StringUtil.padding
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 19/01/19.
 */

class ContentDetailController @Inject constructor(private val logger: Logger) : BaseEpoxyController() {

    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state: ContentDetailViewState by observable(ContentDetailViewState(), ::requestModelBuild)

    override fun buildModels() {
        if (state.isLoading && state.contentDetail == null) {
            buildLoadingState()
        } else {
            buildDetailModels(state)
        }
    }

    private fun buildLoadingState() {
        loader { id("loader") }
    }

    private fun buildDetailModels(viewState: ContentDetailViewState) {
        bookDetail {
            id(viewState.contentDetail?.contentId)
            content(viewState.contentDetail)
            resource(getRandomAuthorResource())
            clickListener { _, parentView, clickedView, _ ->
                clickedView.animateBookOpen()
                (parentView.dataBinding as ItemBookDetailBinding).coverCard2.animateScale()
            }
            reviewsClickListener { _, _, _, _ ->
                callbacks?.onReviewClicked()
            }
            downloadClickListener { _, _, _, _ ->
                callbacks?.onDownloadClicked()
            }
            profileClickListener { _, _, _, _ ->
                callbacks?.onProfileClicked()
            }
            playClickListener { _, _, _, _ ->
                callbacks?.onPlayClicked(viewState.contentDetail)
            }
        }

        viewState.itemsByCreator?.let { list ->
            rowHeader {
                id("row header")
                text(list.title)
            }

            carousel {
                id("suggestions")
                padding(Carousel.Padding.dp(12, 12))
                withModelsFrom(list.contents ?: arrayListOf()) {
                    BookBindingModel_()
                        .id(it.contentId)
                        .content(it)
                        .resource(getRandomAuthorResource())
                        .itemClickListener { _, _, clickedView, _ ->
                            callbacks?.onItemClicked(it, SharedElementHelper().apply {
                                addSharedElement(clickedView, "poster")
                            })
                        }
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
        fun onItemClicked(content: Content, sharedElements: SharedElementHelper)
        fun onPlayClicked(content: ContentDetail?)
    }
}

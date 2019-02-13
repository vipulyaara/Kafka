package com.airtel.kafkapp.feature.detail

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.airtel.data.data.db.entities.Book
import com.airtel.kafkapp.ItemBookBindingModel_
import com.airtel.kafkapp.extensions.getRandomCoverResource
import com.airtel.kafkapp.itemBookDetail
import com.airtel.kafkapp.itemRowHeader
import com.airtel.kafkapp.ui.epoxy.carousel
import com.airtel.kafkapp.ui.epoxy.withModelsFrom

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
class BookDetailController(
    private val callbacks: Callbacks
) : TypedEpoxyController<Book>() {
    override fun buildModels(data: Book?) {
        itemBookDetail {
            id(data?.id)
            book(data)
            clickListener { _, _, clickedView, _ ->
                clickedView.animateBookOpen()
            }
            reviewsClickListener { _, _, _, _ ->
                callbacks.onReviewsClicked()
            }
        }

        itemRowHeader {
            id("row header")
            text("Related books")
        }

        carousel {
            id("suggestions")
            padding(Carousel.Padding.dp(12, 12))
            withModelsFrom(arrayListOf(1, 2, 3, 4, 5, 6, 78, 9, 10, 11, 12, 13, 14, 15)) {
                ItemBookBindingModel_()
                    .id(it)
                    .resource(getRandomCoverResource())
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

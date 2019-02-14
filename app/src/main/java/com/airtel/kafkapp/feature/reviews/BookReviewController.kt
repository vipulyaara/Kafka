package com.airtel.kafkapp.feature.reviews

import com.airbnb.epoxy.TypedEpoxyController
import com.airtel.data.entities.Book
import com.airtel.kafkapp.itemReview
import com.airtel.kafkapp.itemRowHeader

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
class BookReviewController(
    private val callbacks: Callbacks
) : TypedEpoxyController<Book>() {
    override fun buildModels(data: Book?) {
        itemRowHeader {
            id("row header")
            text("Reviews")
        }

        arrayListOf(1, 2, 3, 4, 5, 6, 7).forEach {
            itemReview {
                id("review $it")
            }
        }
    }

    interface Callbacks {
        fun onReviewsClicked()
    }
}

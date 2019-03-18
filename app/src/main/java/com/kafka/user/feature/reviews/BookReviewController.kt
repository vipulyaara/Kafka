package com.kafka.user.feature.reviews

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.data.entities.Book
import com.kafka.user.itemReview
import com.kafka.user.itemRowHeader

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

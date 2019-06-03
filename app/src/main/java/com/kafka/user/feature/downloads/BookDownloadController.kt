package com.kafka.user.feature.downloads

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.data.entities.Book
import com.kafka.user.itemDownload
import com.kafka.user.itemReview
import com.kafka.user.itemRowHeader

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
class BookDownloadController(
    private val callbacks: Callbacks
) : TypedEpoxyController<Book>() {
    override fun buildModels(data: Book?) {
        itemRowHeader {
            id("row header")
            text("Download")
        }

        arrayListOf(1, 2, 3, 4, 5, 6, 7).forEach {
            itemDownload {
                id("review $it")
            }
        }
    }

    interface Callbacks {
        fun onReviewsClicked()
    }
}

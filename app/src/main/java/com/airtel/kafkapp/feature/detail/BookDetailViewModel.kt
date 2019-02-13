package com.airtel.kafkapp.feature.detail

import com.airtel.kafka.Kafka
import com.airtel.kafkapp.feature.common.BaseViewModel

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
class BookDetailViewModel : BaseViewModel<BookDetailViewState>(
    BookDetailViewState()
) {

    internal fun contentDetailRequest(contentId: String?) = Kafka
        .getBookDetail(contentId ?: "")
}

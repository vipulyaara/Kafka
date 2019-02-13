package com.airtel.kafkapp.feature.home

import com.airtel.kafka.Kafka
import com.airtel.kafkapp.feature.common.BaseViewModel

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
class BooksViewModel : BaseViewModel<BooksViewState>(
    BooksViewState()
) {
    internal fun suggestedContent(authorLastName: String?) = Kafka
        .getSuggestedContent(authorLastName ?: "")
}

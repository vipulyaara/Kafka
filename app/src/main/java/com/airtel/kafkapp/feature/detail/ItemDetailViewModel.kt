package com.airtel.kafkapp.feature.detail

import com.airtel.kafka.Kafka
import com.airtel.kafkapp.feature.common.BaseViewModel

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
class ItemDetailViewModel : BaseViewModel<ItemDetailViewState>(
    ItemDetailViewState()
) {

    internal fun itemDetailRequest(itemId: String?) = Kafka
        .getItemDetail(itemId ?: "")

    internal fun itemsByCreatorRequest(author: String?) = Kafka
        .getSuggestedContent(author ?: "")
}

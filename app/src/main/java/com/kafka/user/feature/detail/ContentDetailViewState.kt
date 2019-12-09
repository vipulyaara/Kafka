package com.kafka.user.feature.detail

import com.kafka.data.entities.ContentDetail
import com.kafka.data.model.RailItem
import com.kafka.user.feature.common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class ContentDetailViewState(
    val contentId: String = "",
    val contentDetail: ContentDetail? = null,
    val itemsByCreator: RailItem? = null,
    val isLoading: Boolean = true
) : BaseViewState {
    constructor(id: String) : this(contentId = id)
}

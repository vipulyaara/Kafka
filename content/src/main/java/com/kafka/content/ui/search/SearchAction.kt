package com.kafka.content.ui.search

import com.kafka.data.entities.Item
import com.kafka.ui_common.action.Action

sealed class SearchAction : Action {
    class ItemDetailAction(val item: Item) : SearchAction()
    class SubmitQueryAction(val query: SearchQuery) : SearchAction()
}

package com.kafka.content.ui.detail

import com.kafka.data.entities.Item
import com.kafka.ui_common.action.Action

sealed class ItemDetailAction: Action {
    class RelatedItemClick(val item: Item) : ItemDetailAction()
    class Play(val itemId: String) : ItemDetailAction()
    class Read(val readerUrl: String, val title: String = "") : ItemDetailAction()
    class AuthorClick(val authorId: String) : ItemDetailAction()
    object RatingWidgetClick : ItemDetailAction()
    object FavoriteClick : ItemDetailAction()
}

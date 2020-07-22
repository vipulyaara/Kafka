package com.kafka.ui.detail

import com.data.base.model.item.File
import com.kafka.data.entities.Item
import com.kafka.ui_common.action.Action

sealed class ItemDetailAction: Action {
    class RelatedItemClick(val item: Item) : ItemDetailAction()
    class Play(val file: File? = null) : ItemDetailAction()
    class Read(val readerUrl: String) : ItemDetailAction()
    class AuthorClick(val authorId: String) : ItemDetailAction()
    object RatingWidgetClick : ItemDetailAction()
    object FavoriteClick : ItemDetailAction()
}

package com.kafka.ui.detail

import com.kafka.data.entities.Item
import com.kafka.data.model.item.File

sealed class ItemDetailAction {
    class RelatedItemClick(val item: Item) : ItemDetailAction()
    class Play(val file: File? = null) : ItemDetailAction()
    class Read(val file: File? = null) : ItemDetailAction()
    class AuthorClick(val authorId: String) : ItemDetailAction()
    object RatingWidgetClick : ItemDetailAction()
    object FavoriteClick : ItemDetailAction()
}

package com.kafka.ui.content

sealed class ContentDetailAction {
    class ContentItemClick(val contentId: String) : ContentDetailAction()
    class AuthorClick(val authorId: String) : ContentDetailAction()
    class RatingWidgetClick() : ContentDetailAction()
}
package com.kafka.ui.search

sealed class SearchAction

class ContentItemClick(val contentId: String) : SearchAction()

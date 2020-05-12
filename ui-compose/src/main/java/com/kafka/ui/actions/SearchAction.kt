package com.kafka.ui.actions

import com.kafka.data.entities.Item

sealed class SearchAction

class ItemClickAction(val item: Item) : SearchAction()
class SubmitQueryAction(val query: String) : SearchAction()

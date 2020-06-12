package com.kafka.ui.actions

import com.kafka.data.entities.Item

sealed class HomepageAction

class ItemClickAction(val item: Item) : HomepageAction()
class SubmitQueryAction(val query: String) : HomepageAction()

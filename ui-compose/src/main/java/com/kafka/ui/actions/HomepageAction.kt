package com.kafka.ui.actions

import com.kafka.data.entities.Item

sealed class HomepageAction

class ItemDetailAction(val item: Item) : HomepageAction()
class SubmitQueryAction(val query: String) : HomepageAction()
class UpdateHomepageAction() : HomepageAction()

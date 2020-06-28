package com.kafka.ui.actions

import com.kafka.data.entities.Item
import com.kafka.ui.search.SearchQuery

sealed class HomepageAction

class ItemDetailAction(val item: Item) : HomepageAction()
class SubmitQueryAction(val query: SearchQuery) : HomepageAction()
class UpdateHomepageAction() : HomepageAction()

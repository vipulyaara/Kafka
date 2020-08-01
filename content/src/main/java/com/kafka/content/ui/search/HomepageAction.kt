package com.kafka.content.ui.search

import com.kafka.data.entities.Item
import com.kafka.ui_common.action.Action

sealed class HomepageAction : Action

class ItemDetailAction(val item: Item) : HomepageAction()
class SubmitQueryAction(val query: com.kafka.content.ui.search.SearchQuery) : HomepageAction()
class UpdateHomepageAction() : HomepageAction()

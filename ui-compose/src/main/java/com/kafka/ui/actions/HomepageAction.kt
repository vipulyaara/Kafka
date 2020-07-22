package com.kafka.ui.actions

import com.kafka.data.entities.Item
import com.kafka.ui.search.SearchQuery
import com.kafka.ui_common.action.Action

sealed class HomepageAction : Action

class ItemDetailAction(val item: Item) : HomepageAction()
class SubmitQueryAction(val query: SearchQuery) : HomepageAction()
class UpdateHomepageAction() : HomepageAction()

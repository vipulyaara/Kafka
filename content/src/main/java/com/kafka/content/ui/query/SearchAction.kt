package com.kafka.content.ui.query

import android.view.View
import com.kafka.data.entities.Item
import com.kafka.ui_common.action.Action

sealed class SearchAction : Action {
    class ItemDetailAction(val item: Item) : SearchAction()
    class ItemDetailWithSharedElement(val item: Item, val view: View) : SearchAction()
    class SubmitQueryAction(val query: SearchQuery) : SearchAction() {
        companion object {
            fun titleOrCreatorAction(text: String) =
                SearchAction.SubmitQueryAction(SearchQuery(text, SearchQueryType.TitleOrCreator))
            fun creatorAction(text: String) =
                SearchAction.SubmitQueryAction(SearchQuery(text, SearchQueryType.Creator))

        }
    }
}

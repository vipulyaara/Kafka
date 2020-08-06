package com.kafka.content.ui.search

import android.widget.EditText
import coil.api.clear
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.book
import com.kafka.content.databinding.ItemBookBinding
import com.kafka.content.emptyState
import com.kafka.content.loader
import com.kafka.content.searchView
import com.kafka.content.ui.ActionListener
import com.kafka.data.entities.Item
import com.kafka.data.extensions.letEmpty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchController @Inject constructor() : TypedEpoxyController<SearchViewState>() {
    lateinit var searchActioner: Channel<SearchAction>

    override fun buildModels(data: SearchViewState?) {
        data?.apply {
            loading(data)
            items?.letEmpty { searchResults(it) }
        }
    }

    private fun empty(searchViewState: SearchViewState) {
        if (!searchViewState.isLoading && searchViewState.items.isNullOrEmpty()) {
            emptyState {
                id("empty")
                text("No results found")
            }
        }
    }

    private fun loading(searchViewState: SearchViewState) {
        if (searchViewState.isLoading && searchViewState.items.isNullOrEmpty()) {
            loader { id("loading") }
        }
    }

    private fun search() {
        searchView {
            id("search")
            actionListener(object : ActionListener {
                override fun onAction(editText: EditText) {
                    sendAction(SearchAction.SubmitQueryAction(SearchQuery(editText.text.toString())))
                }
            })
        }
    }

    private fun searchResults(it: List<Item>) {
        it.forEach { item ->
            book {
                onUnbind { _, view ->
                    (view.dataBinding as ItemBookBinding).heroImage.clear()
                }
                id(item.itemId)
                item(item)
                clickListener { _ -> sendAction(SearchAction.ItemDetailAction(item)) }
            }
        }
    }

    private fun sendAction(searchAction: SearchAction) = GlobalScope.launch {
        searchActioner.send(searchAction)
    }
}

package com.kafka.content.ui.search

import coil.api.clear
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.content.databinding.ItemBookBinding
import com.kafka.content.ui.query.ArchiveQueryViewState
import com.kafka.content.ui.query.SearchAction
import com.kafka.content.ui.query.SearchQuery
import com.kafka.content.ui.query.SearchQueryType
import com.kafka.data.entities.Item
import com.kafka.data.extensions.letEmpty
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class SearchController @Inject constructor() : TypedEpoxyController<ArchiveQueryViewState>() {
    lateinit var searchActioner: Channel<SearchAction>

    override fun buildModels(data: ArchiveQueryViewState?) {
        data?.apply {
            recentSearches(data)
            items?.letEmpty { searchResults(it) }
            loading(data)
        }
    }

    private fun recentSearches(data: ArchiveQueryViewState) {
        data.recentSearches?.forEach {
            recentSearchItem {
                id(it)
                text(it)
                clickListener { _ ->
                    searchActioner.sendAction(
                        SearchAction.SubmitQueryAction(SearchQuery(it, SearchQueryType.TitleOrCreator))
                    )
                }
            }
        }
    }

    private fun loading(archiveQueryViewState: ArchiveQueryViewState) {
        if (archiveQueryViewState.isLoading && archiveQueryViewState.items.isNullOrEmpty()) {
            loader { id("loading") }
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
                clickListener { _ -> searchActioner.sendAction(SearchAction.ItemDetailAction(item)) }
            }
        }
    }

    private fun empty(archiveQueryViewState: ArchiveQueryViewState) {
        if (!archiveQueryViewState.isLoading && archiveQueryViewState.items.isNullOrEmpty()) {
            emptyState {
                id("empty")
                text("No results found")
            }
        }
    }

}

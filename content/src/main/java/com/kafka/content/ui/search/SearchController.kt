package com.kafka.content.ui.search

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.RecentSearchBindingModel_
import com.kafka.content.book
import com.kafka.content.header
import com.kafka.ui_common.action.Actioner
import com.kafka.ui_common.ui.kafkaCarousel
import com.kafka.ui_common.ui.withModelsFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchController : TypedEpoxyController<SearchViewState>() {
    lateinit var actioner: Actioner<HomepageAction>
    override fun buildModels(data: SearchViewState?) {

        header {
            id("header")
            text("Explore")
        }

        val items = data?.homepageItems?.values?.map { it.items }?.flatten()

        items?.distinctBy { it.creator }?.map { it.creator }?.let {
            kafkaCarousel {
                id("favorites")
                padding(Carousel.Padding.dp(12, 8, 96, 24, 12))
                withModelsFrom(it) {
                    RecentSearchBindingModel_().apply {
                        id(it)
                        text(it)
                        isSelected(true)
                        clickListener { _ ->
                            GlobalScope.launch { actioner.sendAction(
                                SubmitQueryAction(
                                    SearchQuery(
                                        it
                                    )
                                )
                            ) }
                        }
                    }
                }
            }
        }

        items?.forEach {
            book {
                id(it.itemId)
                item(it)
                clickListener { _ ->
                    GlobalScope.launch { actioner.sendAction(
                        ItemDetailAction(
                            it
                        )
                    ) }
                }
            }
        }
    }
}

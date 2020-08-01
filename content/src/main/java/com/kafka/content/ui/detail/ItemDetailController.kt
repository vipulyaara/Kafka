package com.kafka.content.ui.detail

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.*
import com.kafka.ui_common.action.Actioner
import com.kafka.ui_common.ui.kafkaCarousel
import com.kafka.ui_common.ui.withModelsFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemDetailController : TypedEpoxyController<ItemDetailViewState>() {
    lateinit var actioner: Actioner<ItemDetailAction>
    override fun buildModels(data: ItemDetailViewState?) {

        if (data?.isLoading == true && data.itemDetail == null) {
            loader { id("loading") }
        }

        data?.itemDetail?.let { detail ->
            detail {
                id(detail.itemId)
                itemDetail(detail)
            }

            detailActions {
                id("detail_actions")
                favoriteClickListener { _ ->
                    GlobalScope.launch {
                        actioner.sendAction(ItemDetailAction.FavoriteClick)
                    }
                }
                playClickListener { _ -> GlobalScope.launch { actioner.sendAction(ItemDetailAction.Play()) } }
            }

            data.itemsByCreator?.let {
                if (it.isNotEmpty()) {
                    sectionHeader {
                        id("section_header")
                        text("More by ${data.itemDetail.creator}")
                    }
                }
                kafkaCarousel {
                    id("related")
                    withModelsFrom(it) {
                        BookGridBindingModel_().apply {
                            id(it.itemId)
                            item(it)
                        }
                    }
                }
            }
        }
    }
}

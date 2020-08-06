package com.kafka.content.ui.detail

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.TypedEpoxyController
import com.data.base.extensions.debug
import com.kafka.content.*
import com.kafka.data.entities.Item
import com.kafka.data.entities.isAudio
import com.kafka.data.entities.readerUrl
import com.kafka.ui_common.action.Actioner
import com.kafka.ui_common.ui.kafkaCarousel
import com.kafka.ui_common.ui.withModelsFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemDetailController : TypedEpoxyController<ItemDetailViewState>() {
    lateinit var actioner: Actioner<ItemDetailAction>
    override fun buildModels(data: ItemDetailViewState?) {

        debug { "loading ${data?.isLoading} items ${data?.itemDetail}" }

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
                isFavorite(data.isFavorite)
                isAudio(detail.isAudio())
                favoriteClickListener { _ -> sendAction(ItemDetailAction.FavoriteClick) }
                playClickListener { _ ->
                    if (detail.isAudio()) {
                        sendAction(ItemDetailAction.Play(detail.itemId))
                    } else {
                        sendAction(ItemDetailAction.Read(detail.readerUrl()))
                    }
                }
            }

            detail.metadata?.let { tags(it) }

            data.itemsByCreator?.let {
                itemsByCreator(it, data.itemDetail.creator)
            }
        }
    }

    private fun tags(tags: List<String>) {
        kafkaCarousel {
            id("tags")
            padding(Carousel.Padding.dp(24, 0, 24, 0, 12))
            withModelsFrom(tags) {
                LabelTagBindingModel_().apply {
                    id(it)
                    text(it)
                }
            }
        }

        verticalSpacingLarge { id("tags_bottom_padding") }
    }

    private fun itemsByCreator(it: List<Item>, creatorName: String?) {
        if (it.isNotEmpty()) {
            sectionHeader {
                id("section_header")
                text("More by $creatorName")
            }
        }

        it.forEach {
            book {
                id(it.itemId)
                item(it)
                clickListener { _ -> sendAction(ItemDetailAction.RelatedItemClick(it)) }
            }
        }
    }

    private fun sendAction(itemDetailAction: ItemDetailAction) =
        GlobalScope.launch {
            actioner.sendAction(itemDetailAction)
        }
}

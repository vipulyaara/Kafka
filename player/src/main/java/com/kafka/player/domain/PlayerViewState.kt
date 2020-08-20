package com.kafka.player.domain

import com.kafka.data.entities.ItemDetail
import com.kafka.player.playback.model.MediaItem
import com.kafka.ui_common.action.Action
import com.kafka.ui_common.base.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class PlayerViewState(
    val isLoading: Boolean = false,
    val mediaItem: MediaItem? = null,
    val isFavorite: Boolean = false,
    val itemDetail: ItemDetail? = null
) : BaseViewState

sealed class PlayerAction : Action {
    data class Command(val playerCommand: PlayerCommand): PlayerAction()
    object FavoriteClick : PlayerAction()
}

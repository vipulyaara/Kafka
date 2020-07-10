package com.kafka.ui.player

import com.kafka.data.entities.ItemDetail
import com.kafka.ui.R
import com.kafka.ui_common.BaseViewState

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class PlayerViewState(
    val isLoading: Boolean = false,
    val playerData: PlayerData? = null,
    val itemDetail: ItemDetail? = null
) : BaseViewState

data class PlayerData(
    val contentId: String? = null,
    val isPlaying: Boolean = false,
    val title: String? = "aah ko chaahiye ik umr",
    val subtitle: String? = "Mirza Ghalib"
)

fun PlayerData.isValid() = contentId != null

fun PlayerData.playIcon() = if (isPlaying) R.drawable.ic_play else R.drawable.ic_pause

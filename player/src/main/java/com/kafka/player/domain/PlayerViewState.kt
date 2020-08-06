package com.kafka.player.domain

import com.kafka.data.entities.ItemDetail
import com.kafka.player.R
import com.kafka.ui_common.base.BaseViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author Vipul Kumar; dated 27/12/18.
 */
data class PlayerViewState(
    val isLoading: Boolean = false,
    val playerData: PlayerData? = null,
    val itemDetail: ItemDetail? = null
) : BaseViewState

data class PlayerData(
    val itemId: Long? = null,
    val isPlaying: Boolean = false,
    val seekFlow: Flow<Int> = MutableStateFlow(0),
    val title: String? = "aah ko chaahiye ik umr",
    val subtitle: String? = "Mirza Ghalib",
    val coverImage: String? = null
)

fun PlayerData.isValid() = itemId != null

fun PlayerData.playIcon() = if (isPlaying) R.drawable.ic_play else R.drawable.ic_pause

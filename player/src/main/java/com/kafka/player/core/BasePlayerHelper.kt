package com.kafka.player.core

import com.kafka.player.model.PlayerState

/**
 * @author Vipul Kumar; dated 05/03/19.
 */
class BasePlayerHelper {

    fun isActionValid(playerAction: BasePlayer.PlayerAction, playerState: PlayerState): Boolean {
        var isValid = false
        if (validStateTransitions[playerAction] != null) {
            for (i in 0 until validStateTransitions.getValue(playerAction).size) {
                if (validStateTransitions.getValue(playerAction)[i]::class.java == playerState::class.java) {
                    isValid = true
                    break
                }
            }
        }
        return isValid
    }

    private val validStateTransitions: Map<BasePlayer.PlayerAction, List<PlayerState>> = mapOf(
        BasePlayer.PlayerAction.INIT to listOf(
            PlayerState.Stopped(true),
            PlayerState.Playing(-1),
            PlayerState.Paused,
            PlayerState.Error(-1, "", null),
            PlayerState.Buffering(true),
            PlayerState.Finished(-1)
        ),
        BasePlayer.PlayerAction.PLAY to listOf(
            PlayerState.Idle,
            PlayerState.Paused,
            PlayerState.Buffering(true),
            PlayerState.Playing(-1),
            PlayerState.Finished(-1)
        ),
        BasePlayer.PlayerAction.PAUSE to listOf(
            PlayerState.Playing(-1),
            PlayerState.Buffering(true),
            PlayerState.Paused
        ),
        BasePlayer.PlayerAction.STOP to listOf(
            PlayerState.Idle,
            PlayerState.Playing(-1),
            PlayerState.Paused,
            PlayerState.Buffering(true)
        ),
        BasePlayer.PlayerAction.SEEK to listOf(
            PlayerState.Idle,
            PlayerState.Playing(-1),
            PlayerState.Paused,
            PlayerState.Buffering(true),
            PlayerState.Finished(-1)
        ),
        BasePlayer.PlayerAction.CHANGE_VOLUME to listOf(
            PlayerState.Playing(-1),
            PlayerState.Paused,
            PlayerState.Buffering(true)
        ),
        BasePlayer.PlayerAction.DESTROY to listOf(
            PlayerState.Idle,
            PlayerState.Playing(-1),
            PlayerState.Paused,
            PlayerState.Finished(-1),
            PlayerState.Error(-1, "", null),
            PlayerState.Buffering(true),
            PlayerState.Stopped(true)
        )
    )
}

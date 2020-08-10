package com.kafka.player.exo.model

internal data class PlayerMediaEntity(
    val mediaEntity: MediaEntity,
    val positionInQueue: PositionInQueue,
    val bookmark: Long
)

internal enum class PositionInQueue {
    /**
     * Has only next songs
     */
    FIRST,
    /**
     * Has previous and next songs
     */
    IN_MIDDLE, // has previous and next songs
    /**
     * Has only previous songs
     */
    LAST,
    /**
     * Is the only song the list, has no previous or next songs
     */
    FIRST_AND_LAST
}

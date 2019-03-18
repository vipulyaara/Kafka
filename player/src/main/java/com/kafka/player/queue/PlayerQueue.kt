package com.kafka.player.queue

import com.kafka.player.model.PlaybackItem
import com.kafka.player.model.PlayerHead

interface PlayerQueue {

    fun init(item: PlaybackItem, head: PlayerHead?)

    fun currentSong(): PlaybackItem?

    fun head(): PlayerHead?

    fun setHead(head: PlayerHead?)

    fun next()

    fun prev()

    fun enqueue(item: PlaybackItem)

    fun enqueue(item: PlaybackItem, song: PlaybackItem)

    fun enqueueSong(item: PlaybackItem)

    fun dequeue(item: PlaybackItem)

    fun size(): Int

    fun clear()

    fun upcomingSongs(): Pair<PlaybackItem, PlaybackItem>?

    fun currentPlaylist(): PlaybackItem?

    fun queueItems(): List<PlaybackItem>?

    fun hasNext(): Boolean

    fun hasPrev(): Boolean
}

package com.kafka.player.playback.extensions

import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.kafka.data.entities.Song

fun List<Song>.toMediaItems() = map { it.toMediaItem() }

fun Song.toMediaItem() = MediaItem.Builder().setUri(playbackUrl).setMediaId(id).setTag(this).build()

fun List<Song>.toMediaSources(dataSourceFactory: DataSource.Factory) =
    map { it.toMediaSource(dataSourceFactory) }

fun Song.toMediaSource(dataSourceFactory: DataSource.Factory) =
    ProgressiveMediaSource.Factory(dataSourceFactory).setTag(this).createMediaSource(this.playbackUrl.toUri())

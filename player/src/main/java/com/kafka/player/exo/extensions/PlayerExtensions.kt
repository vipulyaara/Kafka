package com.kafka.player.exo.extensions

import android.net.Uri
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.kafka.player.exo.model.MediaItem

operator fun MediaSource.plus(other: MediaSource): ConcatenatingMediaSource {
    val source = ConcatenatingMediaSource()
    source.addMediaSource(this)
    source.addMediaSource(other)
    return source
}

operator fun ConcatenatingMediaSource.minus(atIndex: Int): ConcatenatingMediaSource {
    this.removeMediaSource(atIndex)
    return this
}

operator fun ConcatenatingMediaSource.plus(other: MediaSource): ConcatenatingMediaSource {
    this.addMediaSource(other)
    return this
}

operator fun ConcatenatingMediaSource.plusAssign(other: MediaSource) {
    this + other
}

operator fun ConcatenatingMediaSource.minusAssign(atIndex: Int) {
    this - atIndex
}

operator fun ConcatenatingMediaSource.get(index: Int): MediaSource? {
    return this.getMediaSource(index)
}

inline fun ExoPlayer.prepare(mediaSource: MediaSource, config: ExoPlayer.() -> Unit) {
    config()
    this.prepare(mediaSource)
}

operator fun Uri.plus(otherUri: Uri): MutableList<Uri> {
    return mutableListOf(this, otherUri)
}

operator fun MutableList<Uri>.plus(otherUri: Uri): MutableList<Uri> {
    this.add(otherUri)
    return this
}

fun List<Uri>.toMediaSource(factory: ProgressiveMediaSource.Factory): ConcatenatingMediaSource {
    val source = ConcatenatingMediaSource()
    this.forEach {
        source += it.toMediaSource(factory)
    }
    return source
}

fun Uri.toMediaSource(factory: ProgressiveMediaSource.Factory): MediaSource {
    return factory.setTag(this).createMediaSource(this)
}

fun List<MediaItem>.toMediaSources(dataSourceFactory: DataSource.Factory) =
    map { it.playbackUrl.toUri().toMediaSource(ProgressiveMediaSource.Factory(dataSourceFactory)) }

fun MediaItem.toMediaSource(dataSourceFactory: DataSource.Factory) =
    playbackUrl.toUri().toMediaSource(ProgressiveMediaSource.Factory(dataSourceFactory))

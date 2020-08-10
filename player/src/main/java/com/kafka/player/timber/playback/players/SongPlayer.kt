/*
 * Copyright (c) 2019 Naman Dwivedi.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package com.kafka.player.timber.playback.players

import android.app.Application
import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import androidx.core.net.toUri
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.data.base.extensions.debug
import com.kafka.data.dao.QueueDao
import com.kafka.data.entities.QueueEntity
import com.kafka.player.R
import com.kafka.player.timber.MusicUtils
import com.kafka.player.timber.constants.Constants.ACTION_REPEAT_QUEUE
import com.kafka.player.timber.constants.Constants.ACTION_REPEAT_SONG
import com.kafka.player.timber.constants.Constants.REPEAT_MODE
import com.kafka.player.timber.constants.Constants.SHUFFLE_MODE
import com.kafka.player.timber.extensions.asString
import com.kafka.player.timber.extensions.position
import com.kafka.player.timber.extensions.toSongIDs
import com.kafka.player.timber.models.Song
import com.kafka.player.timber.repository.SongsRepository
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A wrapper around [MusicPlayer] that specifically manages playing [Song]s and
 * links up with [Queue].
 *
 * @author Aidan Follestad (@afollestad)
 */

interface SongPlayer {

    fun setQueue(
        data: LongArray = LongArray(0),
        title: String = ""
    )

    fun getSession(): MediaSessionCompat

    fun playSong()

    fun playSong(id: Long)

    fun playSong(song: Song)

    fun seekTo(position: Int)

    fun pause()

    fun nextSong()

    fun repeatSong()

    fun repeatQueue()

    fun previousSong()

    fun playNext(id: Long)

    fun swapQueueSongs(from: Int, to: Int)

    fun removeFromQueue(id: Long)

    fun stop()

    fun release()

    fun addStateChangeListener(state: (PlaybackStateCompat) -> Unit)

    fun onPrepared(prepared: OnPrepared<SongPlayer>)

    fun onError(error: OnError<SongPlayer>)

    fun onCompletion(completion: OnCompletion<SongPlayer>)

    fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit)

    fun setPlaybackState(state: PlaybackStateCompat)

    fun restoreFromQueueData(queueData: QueueEntity)

    val songChannel: ConflatedBroadcastChannel<Song>

    val seekPositionFlow: Flow<Int>
}

@Singleton
class RealSongPlayer @Inject constructor(
    private val context: Application,
    private val musicPlayer: MusicPlayer,
    private val songsRepository: SongsRepository,
    private val queueDao: QueueDao,
    private val queue: Queue
) : SongPlayer {

    private var isInitialized: Boolean = false

    private var preparedCallback: OnPrepared<SongPlayer> = {}
    private var errorCallback: OnError<SongPlayer> = {}
    private var completionCallback: OnCompletion<SongPlayer> = {}

    private var metadataBuilder = MediaMetadataCompat.Builder()
    private var stateBuilder = createDefaultPlaybackState()

    private val stateListeners = mutableListOf<(PlaybackStateCompat) -> Unit>()

    private var mediaSession = MediaSessionCompat(context, context.getString(R.string.app_name)).apply {
        setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        setCallback(MediaSessionCallback(this, this@RealSongPlayer, songsRepository, queueDao))
        setPlaybackState(stateBuilder.build())

        val sessionIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val sessionActivityPendingIntent = PendingIntent.getActivity(context, 0, sessionIntent, 0)
        setSessionActivity(sessionActivityPendingIntent)
        isActive = true
    }

    init {
        queue.setMediaSession(mediaSession)

        musicPlayer.onPrepared {
            preparedCallback(this@RealSongPlayer)
            playSong()
            seekTo(getSession().position().toInt())
        }

        musicPlayer.onCompletion {
            completionCallback(this@RealSongPlayer)
            val controller = getSession().controller
            when (controller.repeatMode) {
                REPEAT_MODE_ONE -> {
                    controller.transportControls.sendCustomAction(ACTION_REPEAT_SONG, null)
                }
                REPEAT_MODE_ALL -> {
                    controller.transportControls.sendCustomAction(ACTION_REPEAT_QUEUE, null)
                }
                else -> controller.transportControls.skipToNext()
            }
        }
    }

    override fun setQueue(
        data: LongArray,
        title: String
    ) {
        Timber.d("""setQueue: ${data.asString()} ("$title"))""")
        this.queue.ids = data
        this.queue.title = title
    }

    override fun getSession(): MediaSessionCompat = mediaSession

    override fun playSong() {
        Timber.d("playSong()")
        queue.ensureCurrentId()

        if (isInitialized) {
            updatePlaybackState {
                setState(STATE_PLAYING, mediaSession.position(), 1F)
            }
            musicPlayer.play()
            return
        }
        musicPlayer.reset()

        val path = MusicUtils.getSongUri(queue.currentSongId)
        val isSourceSet = if (path.startsWith("content://")) {
            musicPlayer.setSource(path.toUri())
        } else {
            musicPlayer.setSource(path)
        }
        if (isSourceSet) {
            isInitialized = true
            musicPlayer.prepare()
        }
    }

    override fun playSong(id: Long) {
        Timber.d("playSong(): $id")
        val song = songsRepository.getSongForId(id)
        playSong(song)
    }

    override fun playSong(song: Song) {
        Timber.d("playSong(): ${song.title}")
        if (queue.currentSongId != song.id) {
            queue.currentSongId = song.id
            isInitialized = false
            updatePlaybackState {
                setState(STATE_STOPPED, 0, 1F)
            }
        }
        setMetaData(song)
        playSong()
    }

    override fun seekTo(position: Int) {
        Timber.d("seekTo(): $position")
        if (isInitialized) {
            musicPlayer.seekTo(position)
            updatePlaybackState {
                setState(
                    mediaSession.controller.playbackState.state,
                    position.toLong(),
                    1F
                )
            }
        }
    }

    override fun pause() {
        Timber.d("pause()")
        if (musicPlayer.isPlaying() && isInitialized) {
            musicPlayer.pause()
            updatePlaybackState {
                setState(STATE_PAUSED, mediaSession.position(), 1F)
            }
        }
    }

    override fun nextSong() {
        Timber.d("nextSong()")
        queue.nextSongId?.let {
            playSong(it)
        } ?: pause()
    }

    override fun repeatSong() {
        Timber.d("repeatSong()")
        updatePlaybackState {
            setState(STATE_STOPPED, 0, 1F)
        }
        playSong(queue.currentSong())
    }

    override fun repeatQueue() {
        Timber.d("repeatQueue()")
        if (queue.currentSongId == queue.lastId())
            playSong(queue.firstId())
        else {
            nextSong()
        }
    }

    override fun previousSong() {
        Timber.d("previousSong()")
        queue.previousSongId?.let(::playSong)
    }

    override fun playNext(id: Long) {
        Timber.d("playNext(): $id")
        queue.moveToNext(id)
    }

    override fun swapQueueSongs(from: Int, to: Int) {
        Timber.d("swapQueueSongs(): $from -> $to")
        queue.swap(from, to)
    }

    override fun removeFromQueue(id: Long) {
        Timber.d("removeFromQueue(): $id")
        queue.remove(id)
    }

    override fun stop() {
        Timber.d("stop()")
        musicPlayer.stop()
        updatePlaybackState {
            setState(STATE_NONE, 0, 1F)
        }
    }

    override fun release() {
        Timber.d("release()")
        mediaSession.apply {
            isActive = false
            release()
        }
        musicPlayer.release()
        queue.reset()
    }

    override fun onPrepared(prepared: OnPrepared<SongPlayer>) {
        this.preparedCallback = prepared
    }

    override fun onError(error: OnError<SongPlayer>) {
        this.errorCallback = error
        musicPlayer.onError { throwable ->
            errorCallback(this@RealSongPlayer, throwable)
        }
    }

    override fun onCompletion(completion: OnCompletion<SongPlayer>) {
        this.completionCallback = completion
    }

    override fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit) {
        applier(stateBuilder)
        setPlaybackState(stateBuilder.build())
    }

    override fun setPlaybackState(state: PlaybackStateCompat) {
        mediaSession.setPlaybackState(state)
        state.extras?.let { bundle ->
            mediaSession.setRepeatMode(bundle.getInt(REPEAT_MODE))
            mediaSession.setShuffleMode(bundle.getInt(SHUFFLE_MODE))
        }

        stateListeners.forEach { it.invoke(state) }
    }

    override fun addStateChangeListener(state: (PlaybackStateCompat) -> Unit) {
        stateListeners.add(state)
    }

    override fun restoreFromQueueData(queueData: QueueEntity) {
        queue.currentSongId = queueData.currentId ?: -1
        val playbackState = queueData.playState ?: STATE_NONE
        val currentPos = queueData.currentSeekPos ?: 0
        val repeatMode = queueData.repeatMode ?: REPEAT_MODE_NONE
        val shuffleMode = queueData.shuffleMode ?: SHUFFLE_MODE_NONE

        val queueIds = queueDao.getQueueSongsSync().toSongIDs()
        setQueue(queueIds, queueData.queueTitle)
        setMetaData(queue.currentSong())

        val extras = Bundle().apply {
            putInt(REPEAT_MODE, repeatMode)
            putInt(SHUFFLE_MODE, shuffleMode)
        }
        updatePlaybackState {
            setState(playbackState, currentPos, 1F)
            setExtras(extras)
        }
    }

    private fun setMetaData(song: Song) {
        // TODO make music utils injectable
        val artwork = MusicUtils.getAlbumArtBitmap(context, song.albumId)
        val mediaMetadata = metadataBuilder.apply {
            putString(METADATA_KEY_ALBUM, song.album)
            putString(METADATA_KEY_ARTIST, song.artist)
            putString(METADATA_KEY_TITLE, song.title)
            putString(METADATA_KEY_ALBUM_ART_URI, song.albumId.toString())
            putBitmap(METADATA_KEY_ALBUM_ART, artwork)
            putString(METADATA_KEY_MEDIA_ID, song.id.toString())
            putLong(METADATA_KEY_DURATION, song.duration.toLong())
        }.build()
        mediaSession.setMetadata(mediaMetadata)

        songChannel.sendBlocking(song)
    }

    override val songChannel: ConflatedBroadcastChannel<Song> = ConflatedBroadcastChannel()

    override val seekPositionFlow: Flow<Int> = queueDao.getQueueCurrentSeekPos().map {
        debug { "Current seek pos $it" }
        it
    }.asFlow()
}

private fun createDefaultPlaybackState(): PlaybackStateCompat.Builder {
    return PlaybackStateCompat.Builder().setActions(
        ACTION_PLAY
                or ACTION_PAUSE
                or ACTION_PLAY_FROM_SEARCH
                or ACTION_PLAY_FROM_MEDIA_ID
                or ACTION_PLAY_PAUSE
                or ACTION_SKIP_TO_NEXT
                or ACTION_SKIP_TO_PREVIOUS
                or ACTION_SET_SHUFFLE_MODE
                or ACTION_SET_REPEAT_MODE
    )
        .setState(STATE_NONE, 0, 1f)
}

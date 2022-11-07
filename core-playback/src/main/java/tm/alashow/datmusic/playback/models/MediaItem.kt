package tm.alashow.datmusic.playback.models

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.net.toUri
import com.kafka.data.entities.Audio
import com.kafka.data.entities.subtitle
import tm.alashow.datmusic.playback.METADATA_KEY_CONTENT_ID
import tm.alashow.datmusic.playback.METADATA_KEY_ID

fun List<MediaSessionCompat.QueueItem>?.toMediaIdList(): List<MediaId> {
    return this?.map { it.description.mediaId?.toMediaId() ?: MediaId() } ?: emptyList()
}

fun List<String>.toMediaIds(): List<MediaId> {
    return this.map { it.toMediaId() }
}

fun List<String>.toMediaAudioIds(): List<String> {
    return this.map { it.toMediaId().value }
}

fun List<Audio?>.toQueueItems(): List<MediaSessionCompat.QueueItem> {
    return filterNotNull().mapIndexed { index, audio ->
        MediaSessionCompat.QueueItem(
            audio.toMediaDescription(),
            (audio.id + index).hashCode().toLong()
        )
    }
}

fun Audio.toMediaDescription(): MediaDescriptionCompat {
    return MediaDescriptionCompat.Builder()
        .setTitle(title)
        .setMediaId(MediaId(MEDIA_TYPE_AUDIO, id).toString())
        .setSubtitle(subtitle)
        .setDescription(subtitle)
        .setIconUri(coverImage?.toUri()).build()
}

fun Audio.toMediaItem(): MediaBrowserCompat.MediaItem {
    return MediaBrowserCompat.MediaItem(
        MediaDescriptionCompat.Builder()
            .setMediaId(MediaId(MEDIA_TYPE_AUDIO, id).toString())
            .setTitle(title)
            .setIconUri(coverImage?.toUri())
            .setSubtitle(subtitle)
            .build(),
        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
    )
}

fun List<Audio>?.toMediaItems() =
    this?.map { it.toMediaItem() } ?: emptyList()

fun Audio.toMediaMetadata(builder: MediaMetadataCompat.Builder): MediaMetadataCompat.Builder =
    builder.apply {
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM, subtitle)
        putString(MediaMetadataCompat.METADATA_KEY_ARTIST, subtitle)
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
        putString(METADATA_KEY_CONTENT_ID, itemId)
        putString(METADATA_KEY_ID, id)
        putString(
            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
            MediaId(MEDIA_TYPE_AUDIO, itemId).toString()
        )
        putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
        putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, coverImage)
        putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null)
    }

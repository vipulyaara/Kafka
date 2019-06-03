//package com.kafka.player.queue
//
//import com.kafka.data.data.annotations.UseSingleton
//import com.kafka.player.com.kafka.player.model.PlaybackItem
//import com.kafka.player.model.PlayerHead
//import com.kafka.player.model.Playlist
//import com.kafka.player.service.PlayerServiceBridge
//
//@UseSingleton
//class PlayerQueueImp : PlayerQueue {
//
//    private var playerHead: PlayerHead? = null
//
//    private var currentSong: PlaybackItem? = null
//
//    private var currentPlaylist: PlaybackItem? = null
//
//    override fun size(): Int {
//        currentPlaylist?.let {
//            it.items?.let {
//                return it.size
//            }
//        }
//        return 0
//    }
//
//    @Synchronized
//    override fun init(item: PlaybackItem, head: PlayerHead?) {
//        updatePlaylist(item)
//        setHead(head)
//    }
//
//    @Synchronized
//    override fun enqueue(item: PlaybackItem) {
//        updatePlaylist(item)
//        if (item is Playlist) {
//        setHead(PlayerHead.getHead(item))
//        }
//    }
//
//    @Synchronized
//    override fun enqueue(item: PlaybackItem, song: PlaybackItem) {
//        updatePlaylist(item)
//        setHead(PlayerHead(item.id, song.id))
//    }
//
//    @Synchronized
//    override fun enqueueSong(item: PlaybackItem) {
//        playerHead?.let {
//            if (it.playlistId == currentPlaylist?.id) {
//                currentSong?.let {
//                    if (it.id == item.id) {
//                        PlayerServiceBridge.getInstance().togglePlayback()
//                        return
//                    }
//                }
//                setHead(PlayerHead(it.playlistId, item.id))
//            }
//        }
//    }
//
//    @Synchronized
//    private fun updatePlaylist(item: PlaybackItem) {
//        currentPlaylist = item
//        WynkEventBus.postEvent(QueueUpdateEvent(size(), item))
//    }
//
//    @Synchronized
//    override fun currentSong(): PlaybackItem? {
//        playerHead?.let {
//            val songId = it.songId
//            val playlistId = it.playlistId
//            if (songId == playlistId) {
//                return currentSong
//            }
//            currentSong?.let {
//                if (it.id == songId)
//                    return currentSong
//            }
//            return getItemById(songId)
//        }
//        return null
//    }
//
//    @Synchronized
//    private fun getItemById(songId: String): PlaybackItem? {
//        queueItems()?.let {
//            for (item in it) {
//                if (item.id == songId) {
//                    currentSong = item
//                    return item
//                }
//            }
//        }
//        return null
//    }
//
//    @Synchronized
//    override fun next() = performQueueAction(Action.NEXT)
//
//    @Synchronized
//    override fun prev() = performQueueAction(Action.PREV)
//
//    @Synchronized
//    override fun clear() {
//        playerHead = null
//        currentPlaylist = null
//        currentSong = null
//    }
//
//    override fun dequeue(item: PlaybackItem) {
//        // Not defined yet
//    }
//
//    override fun queueItems(): List<PlaybackItem>? {
//        currentPlaylist?.let {
//            return it.items
//        }
//        return null
//    }
//
//    @Synchronized
//    override fun setHead(head: PlayerHead?) {
//        playerHead = head
//        currentSong = currentSong()
//        playerHead?.let { WynkEventBus.postEvent(SongChangeEvent(it.playlistId, currentSong)) }
//    }
//
//    override fun upcomingSongs(): Pair<PlaybackItem, PlaybackItem>? {
//        if (size() <= 0) {
//            return null
//        }
//        currentSong()?.let {
//            return if (getNextSong() == it) {
//                Pair(getPrevSong(), it)
//            } else {
//                Pair(it, getNextSong())
//            }
//        }
//        throw IllegalStateException("upcomingSongs(): Current song can not be null. while fetching upcoming song")
//    }
//
//    override fun head(): PlayerHead? = playerHead
//
//    private fun getNextSong(): PlaybackItem {
//        queueItems()?.let {
//            return getSongByPos(it.indexOf(currentSong) + 1)
//        }
//        throw IllegalStateException("Can't find next song on empty queue")
//    }
//
//    private fun getPrevSong(): PlaybackItem {
//        queueItems()?.let {
//            return getSongByPos(it.indexOf(currentSong) - 1)
//        }
//        throw IllegalStateException("Can't find prev song on empty queue")
//    }
//
//    private fun performQueueAction(action: Action) {
//        if (size() == 0) {
//            throw IllegalStateException("performQueueAction(): Queue action can't perform on empty queue. ACTION = ${action.actionName}")
//        }
//        return when (action) {
//            Action.NEXT -> {
//                moveHeadToNext()
//            }
//            Action.PREV -> {
//                moveHeadToPrev()
//            }
//        }
//    }
//
//    private fun moveHeadToPrev() {
//        currentSong?.let {
//            val item = getPrevSong()
//            if (it.id == item.id) {
//                // disable previous button icon if no song available
//            } else {
//                val playlistId = playerHead!!.playlistId
//                setHead(PlayerHead(playlistId, item.id))
//            }
//        }
//    }
//
//    private fun moveHeadToNext() {
//        currentSong?.let {
//            val item = getNextSong()
//            if (it.id == item.id) {
//                // code if you need to play another playlist
//            } else {
//                val playlistId = playerHead!!.playlistId
//                setHead(PlayerHead(playlistId, item.id))
//            }
//        }
//    }
//
//    private fun getSongByPos(pos: Int): PlaybackItem {
//        var nextSonPos = pos
//
//        if (nextSonPos < 0) {
//            nextSonPos = 0
//        }
//        if (nextSonPos >= size()) {
//            nextSonPos = size() - 1
//        }
//        queueItems()?.let {
//            return it[nextSonPos]
//        }
//        throw IllegalStateException("Can't find song on empty queue")
//    }
//
//    override fun hasNext(): Boolean {
//        currentPlaylist?.let {
//            it.items?.let {
//                val currentSongIndex = it.indexOf(currentSong)
//                return currentSongIndex >=0 && currentSongIndex < this@PlayerQueueImp.size()-1
//            }
//
//        }
//        return false
//    }
//
//    override fun hasPrev(): Boolean {
//        currentPlaylist?.let {
//            it.items?.let {
//                return it.indexOf(currentSong) > 0
//            }
//
//        }
//        return false
//    }
//
//    override fun currentPlaylist(): PlaybackItem? = currentPlaylist
//}

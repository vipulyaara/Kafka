//package com.kafka.player.model
//
///**
// * @author Vipul Kumar; dated 05/03/19.
// *
// * Head node for the player queue.
// */
//data class PlayerHead(val playlistId: String, val songId: String) {
//
//    override fun toString(): String {
//        return "$playlistId:$songId"
//    }
//
//    companion object {
//        fun getHead(item: Playlist): PlayerHead {
//            item.audios?.let { return PlayerHead(item.id, it[0].id) }
//            throw IllegalStateException("Unable to create head for empty queueItems()")
//        }
//
//        fun getHead(headStr: String): PlayerHead? {
//            if (headStr.isEmpty()) {
//                return null
//            }
//            val head = headStr.split(":")
//
//            if (head.isEmpty())
//                return null
//            return PlayerHead(head[0], head[1])
//        }
//    }
//}

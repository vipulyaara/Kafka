//package com.kafka.player.service
//
//import android.content.Intent
//import android.support.v4.media.session.MediaSessionCompat
//
//class MediaSessionHelperCallback(val playerService: PlayerService) : MediaSessionCompat.Callback() {
//
//        override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
//            return PlayerServiceIntentReceiver.processMediaButtonIntent(
//                playerService,
//                    mediaButtonEvent
//            )
//        }
//
//        override fun onPause() {
//            playerService.pauseSong()
//        }
//
//        override fun onPlay() {
//            playerService.playSong()
//        }
//
//        override fun onSkipToNext() {
//            playerService.playNextSong()
//        }
//
//        override fun onSkipToPrevious() {
//            playerService.playPrevSong()
//        }
//
//        override fun onStop() {
//            playerService.stopPlayer()
//        }
//    }

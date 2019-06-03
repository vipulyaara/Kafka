package com.kafka.player.model

data class PlayerSeekInfo(val currentPosition: Long,
                          val duration: Long,
                          val bufferedPosition: Long)

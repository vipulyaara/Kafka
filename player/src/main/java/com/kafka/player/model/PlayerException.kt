package com.kafka.player.model

class PlayerException(message: String) :
    Exception(PlayerException::class.java.simpleName + " : " + message)

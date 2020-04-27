package com.kafka.ui.player

sealed class PlayerAction

class Play(val contentId: String) : PlayerAction()

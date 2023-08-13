package com.kafka.remote.config

const val REMOTE_CONFIG_PLAYER_THEME_KEY = "player_theme"

fun RemoteConfig.getPlayerTheme() = get(REMOTE_CONFIG_PLAYER_THEME_KEY)


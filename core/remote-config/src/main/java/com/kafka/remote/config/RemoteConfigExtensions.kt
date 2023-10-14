package com.kafka.remote.config

const val REMOTE_CONFIG_PLAYER_THEME_KEY = "player_theme"
const val IS_SHARE_ENABLED = "is_share_enabled"
const val DOWNLOADER_TYPE = "downloader_type"

fun RemoteConfig.getPlayerTheme() = get(REMOTE_CONFIG_PLAYER_THEME_KEY)

fun RemoteConfig.isShareEnabled() = getBoolean(IS_SHARE_ENABLED)

fun RemoteConfig.downloaderType() = get(DOWNLOADER_TYPE)


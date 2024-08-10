package com.kafka.remote.config

const val REMOTE_CONFIG_PLAYER_THEME_KEY = "player_theme"
const val IS_SHARE_ENABLED = "is_share_enabled"
const val DOWNLOADER_TYPE = "downloader_type"
const val GOOGLE_LOGIN_ENABLED = "google_login_enabled"
const val RECOMMENDATION_ENABLED = "recommendation_data_enabled"
const val VIEW_RECOMMENDATION_ENABLED = "view_recommendation_data_enabled"
const val USE_RECOMMENDATION_ENABLED = "use_recommendation_data_enabled"
const val RECOMMENDATION_ROW_ENABLED = "recommendation_row_enabled"
const val RECOMMENDATION_ROW_INDEX = "recommendation_row_index"
const val RELATED_CONTENT_ROW_ENABLED = "related_content_row_enabled"
const val ONLINE_READER_ENABLED = "online_reader_enabled"
const val MIN_SUPPORTED_VERSION = "min_supported_version"
const val SHARE_APP_INDEX = "share_app_index"
const val DOWNLOADS_WARNING_MESSAGE = "downloads_warning_message"
const val HOMEPAGE_ITEMS_SHUFFLE_ENABLED = "homepage_items_shuffle_enabled"

fun RemoteConfig.getPlayerTheme() = get(REMOTE_CONFIG_PLAYER_THEME_KEY)

fun RemoteConfig.isShareEnabled() = getBoolean(IS_SHARE_ENABLED)

fun RemoteConfig.downloaderType() = get(DOWNLOADER_TYPE)

fun RemoteConfig.isGoogleLoginEnabled() = getBoolean(GOOGLE_LOGIN_ENABLED)

fun RemoteConfig.isRecommendationEnabled() = getBoolean(RECOMMENDATION_ENABLED)

fun RemoteConfig.isViewRecommendationEnabled() = getBoolean(VIEW_RECOMMENDATION_ENABLED)

fun RemoteConfig.isUseRecommendationEnabled() = getBoolean(USE_RECOMMENDATION_ENABLED)

fun RemoteConfig.isRecommendationRowEnabled() = getBoolean(RECOMMENDATION_ROW_ENABLED)

fun RemoteConfig.recommendationRowIndex() = getLong(RECOMMENDATION_ROW_INDEX)

fun RemoteConfig.isRelatedContentRowEnabled() = getBoolean(RELATED_CONTENT_ROW_ENABLED)

fun RemoteConfig.isOnlineReaderEnabled() = getBoolean(ONLINE_READER_ENABLED)

fun RemoteConfig.minSupportedVersion() = getLong(MIN_SUPPORTED_VERSION)

fun RemoteConfig.shareAppIndex() = getLong(SHARE_APP_INDEX)

fun RemoteConfig.downloadsWarningMessage() = get(DOWNLOADS_WARNING_MESSAGE)

fun RemoteConfig.isHomepageItemsShuffleEnabled() = getBoolean(HOMEPAGE_ITEMS_SHUFFLE_ENABLED)
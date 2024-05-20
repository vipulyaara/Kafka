package org.kafka.analytics

import org.kafka.analytics.logger.AnalyticsImpl
import javax.inject.Inject

class EventRepository @Inject constructor() {
    fun searchQuery(keyword: String, filters: List<String>? = null) =
        AnalyticsImpl.SEARCH to mapOf(
            "keyword" to keyword,
            "filters" to filters?.joinToString(),
        )

    fun removeRecentSearch(keyword: String) = "remove_recent_search" to mapOf("keyword" to keyword)

    fun openItemDetail(itemId: String, source: String? = null, name: String? = null) =
        "open_item_detail" to mapOf(
            "item_id" to itemId,
            "source" to source,
            "name" to name,
        )

    fun playItem(itemId: String, source: String? = null, index: Int = 0) = "play_item" to mapOf(
        "item_id" to itemId,
        "source" to source,
        "index" to index.toString(),
    )

    fun readItem(itemId: String, type: String = "offline", source: String? = null) =
        "read_item" to mapOf(
            "item_id" to itemId,
            "type" to type,
            "source" to source,
        )

    fun fileNotSupported(itemId: String) = "file_not_supported" to mapOf("item_id" to itemId)

    fun homeTabSwitched(tab: String, source: String? = null) = "home_tab_switched" to mapOf(
        "tab" to tab,
        "source" to source,
    )

    fun addFavorite(itemId: String, source: String? = null) = "add_favorite" to mapOf(
        "item_id" to itemId,
        "source" to source,
    )

    fun removeFavorite(itemId: String, source: String? = null) = "remove_favorite" to mapOf(
        "item_id" to itemId,
        "source" to source,
    )

    fun openFiles(itemId: String) = "open_files" to mapOf(
        "item_id" to itemId,
    )

    fun downloadFile(fileId: String, itemId: String) = "download_file" to mapOf(
        "item_id" to itemId,
        "file_id" to fileId,
    )

    fun addRecentItem(itemId: String, title: String? = null) = "add_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title,
    )

    fun openRecentItem(itemId: String, title: String? = null) = "open_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title,
    )

    fun openSubject(subject: String, source: String? = null) = "open_subject" to mapOf(
        "subject" to subject,
        "source" to source,
    )

    fun removeRecentItem(itemId: String, title: String? = null) = "remove_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title,
    )

    fun openLogin() = "open_login" to mapOf<String, String>()

    fun signUp(name: String?) = AnalyticsImpl.SIGN_UP to mapOf(
        "name" to name,
    )

    fun login(method: String = "email") = AnalyticsImpl.LOGIN to mapOf(
        AnalyticsImpl.PARAM_METHOD to method,
    )

    fun logoutClicked() = "logout_clicked" to mapOf<String, String>()

    fun shareItem(itemId: String, source: String) = "share_item" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun shareApp() = "share_app" to mapOf<String, String>()

    fun openArchiveItem(itemId: String) = "open_archive_item" to mapOf("item_id" to itemId)

    fun setDownloadLocation(location: String) = "set_download_location" to mapOf(
        "location" to location,
    )

    fun resetDownloadLocation() = "reset_download_location" to mapOf<String, String>()

    fun forgotPasswordSuccess() = "forgot_password_success" to mapOf<String, String>()

    fun themeChanged(theme: String) = "theme_changed" to mapOf("theme" to theme)

    fun trueContrastChanged(trueContrast: Boolean) = "true_contrast_changed" to mapOf(
        "true_contrast" to trueContrast.toString()
    )

    fun safeModeChanged(enabled: Boolean) = "safe_mode_changed" to mapOf(
        "safe_mode_content" to enabled.toString()
    )

    fun openRecentItems() = "open_recent_items" to mapOf<String, String>()

    fun clearRecentItems() = "clear_recent_items" to mapOf<String, String>()

    fun showAppReviewDialog() = "show_review_dialog" to mapOf<String, String>()
}

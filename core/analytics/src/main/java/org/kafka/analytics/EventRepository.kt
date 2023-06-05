package org.kafka.analytics

import javax.inject.Inject

class EventRepository @Inject constructor() {
    fun searchQuery(keyword: String, filters: List<String>? = null) =
        FirebaseAnalytics.SEARCH to mapOf(
            "keyword" to keyword,
            "filters" to filters?.joinToString()
        )

    fun addRecentSearch(keyword: String, filters: List<String>? = null) =
        "add_recent_search" to mapOf(
            "keyword" to keyword,
            "filters" to filters?.joinToString()
        )

    fun removeRecentSearch(keyword: String) = "remove_recent_search" to mapOf("keyword" to keyword)

    fun openItemDetail(itemId: String, source: String? = null) = "open_item_detail" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun playItem(itemId: String, source: String? = null) = "play_item" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun readItem(itemId: String, source: String? = null) = "read_item" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun homeTabSwitched(tab: String, source: String? = null) = "home_tab_switched" to mapOf(
        "tab" to tab,
        "source" to source
    )

    fun addFavorite(itemId: String, source: String? = null) = "add_favorite" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun removeFavorite(itemId: String, source: String? = null) = "remove_favorite" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun openFiles(itemId: String) = "open_files" to mapOf(
        "item_id" to itemId
    )

    fun addRecentItem(itemId: String, title: String? = null) = "add_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun openRecentItem(itemId: String, title: String? = null) = "open_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun removeRecentItem(itemId: String, title: String? = null) = "remove_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun loginClicked() = "login_prompt_clicked" to mapOf<String, String>()

    fun signUp(name: String?) = FirebaseAnalytics.SIGN_UP to mapOf(
        "name" to name
    )

    fun login(method: String) = FirebaseAnalytics.LOGIN to mapOf(
        FirebaseAnalytics.PARAM_METHOD to method
    )

    fun logoutClicked() = "logout_clicked" to mapOf<String, String>()

    fun shareItem(itemId: String) = "share_item" to mapOf("item_id" to itemId)

    fun setDownloadLocation(location: String) = "set_download_location" to mapOf(
        "location" to location
    )

    fun resetDownloadLocation() = "reset_download_location" to mapOf<String, String>()
}

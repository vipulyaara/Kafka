package org.kafka.analytics

import javax.inject.Inject

class EventRepository @Inject constructor(
) {
    fun addRecentSearch(keyword: String, filters: List<String>? = null) =
        "add_recent_search" to mapOf(
            "keyword" to keyword,
            "filters" to filters?.joinToString()
        )

    fun removeRecentSearch(keyword: String) = "remove_recent_search" to mapOf("keyword" to keyword)

    fun itemDetailClick(itemId: String, source: String? = null) = "item_detail_click" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun addFavorite(itemId: String, source: String? = null) = "add_to_favorites" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun removeFavorite(itemId: String, source: String? = null) = "remove_from_favorites" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun openFiles(itemId: String) = "open_files" to mapOf(
        "item_id" to itemId
    )

    fun playAudio(itemId: String, contentTitle: String) = "item_detail_click" to mapOf(
        "item_id" to itemId,
        "content_title" to contentTitle
    )

    fun addRecentItem(itemId: String, title: String? = null) = "add_to_recent" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun removeRecentItem(itemId: String, title: String? = null) = "remove_from_recent" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun loginClicked() = "login_prompt_clicked" to mapOf<String, String>()

    fun logoutClicked() = "logout_clicked" to mapOf<String, String>()

    fun shareItem(itemId: String) = "share_item" to mapOf("item_id" to itemId)
}

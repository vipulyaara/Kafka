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

    fun openItemDetail(itemId: String, source: String? = null) = "open_item_detail" to mapOf(
        "item_id" to itemId,
        "source" to source
    )

    fun updateItemDetail(itemId: String, source: String? = null) = "update_item_detail" to mapOf(
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

    fun addRecentItem(itemId: String, title: String? = null) = "add_to_recent" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun openRecentItem(itemId: String, title: String? = null) = "open_recent_item" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun removeRecentItem(itemId: String, title: String? = null) = "remove_from_recent" to mapOf(
        "item_id" to itemId,
        "title" to title
    )

    fun loginClicked() = "login_prompt_clicked" to mapOf<String, String>()

    fun logNoAnonymousUser() = "null_user" to mapOf(
        "source" to "homepage"
    )

    fun logoutClicked() = "logout_clicked" to mapOf<String, String>()

    fun shareItem(itemId: String) = "share_item" to mapOf("item_id" to itemId)
}

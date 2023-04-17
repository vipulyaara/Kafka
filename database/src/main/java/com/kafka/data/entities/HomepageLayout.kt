package com.kafka.data.entities

data class HomepageLayout(
    val banners: List<Banner>,
    val recentItems: List<Item>,
    val favoriteItems: List<Item>,
    val queryItems: List<String>,
) {
    enum class ActionType { Detail, Collection, Subject }
    
    sealed class ItemType

    data class Banner(val id: String, val imageUrl: String, val type: ActionType) : ItemType()
}

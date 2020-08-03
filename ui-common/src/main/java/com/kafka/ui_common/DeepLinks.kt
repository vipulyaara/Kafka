package com.kafka.ui_common

import androidx.core.net.toUri
import androidx.navigation.NavController

sealed class Navigation {
    object Homepage: Navigation()
    data class ItemDetail(val itemId: String): Navigation()
    data class Player(val itemId: String): Navigation()
    data class Reader(val itemId: String): Navigation()
    object Library : Navigation()
    object LanguageSelection : Navigation()
}

fun NavController.navigate(navigation: Navigation) {
    navigate(DeepLinksNavigations.navigate(navigation))
}

object DeepLinksNavigations {
    fun navigate(navigation: Navigation) = when (navigation) {
            Navigation.Homepage -> { "app.kafka://homepage".toUri()}
            is Navigation.ItemDetail -> { "app.kafka://item/${navigation.itemId}".toUri() }
            is Navigation.Player -> "app.kafka://player/${navigation.itemId}".toUri()
            is Navigation.Reader -> "app.kafka://reader/${navigation.itemId}".toUri()
            is Navigation.Library -> "app.kafka://library".toUri()
            is Navigation.LanguageSelection -> "app.kafka://language".toUri()
        }

}

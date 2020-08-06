package com.kafka.content.ui.profile

import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.content.R
import com.kafka.content.profileHeader
import com.kafka.content.profileItem
import com.kafka.content.separatorLarge
import javax.inject.Inject

class ProfileController @Inject constructor() : TypedEpoxyController<Int>() {
    override fun buildModels(data: Int?) {
        profileHeader { id("header") }

        menuItems.forEach {
            if (it is TextMenuItem) {
                profileItem {
                    id("$it")
                    text(it.text)
                    resource(it.icon)
                }
            }
            if (it is MenuSeparator) {
                separatorLarge { id("sep$it") }
            }
        }
    }
}


sealed class MenuItem
data class TextMenuItem(val icon: Int, val text: String) : MenuItem()
object MenuSeparator : MenuItem()

val menuItems = arrayListOf(
    TextMenuItem(R.drawable.ic_heart_sign, "Favorites"),
    MenuSeparator,
    TextMenuItem(R.drawable.ic_feather, "Poets"),
    TextMenuItem(R.drawable.ic_book, "Sher"),
    TextMenuItem(R.drawable.ic_layers, "Shayari"),
    TextMenuItem(R.drawable.ic_headphones, "Prose"),
    MenuSeparator,
    TextMenuItem(R.drawable.ic_music, "Image Shayari"),
    TextMenuItem(R.drawable.ic_user, "Dictionary"),
    MenuSeparator,
    TextMenuItem(R.drawable.ic_settings, "Settings"),
    TextMenuItem(R.drawable.ic_info, "About us"),
    TextMenuItem(R.drawable.ic_help_circle, "Feedback")
)

package com.kafka.user.ui.menu

import android.view.View

/**
 * @author Vipul Kumar; dated 01/04/19.
 */

open class BaseMenuItem

data class ContextMenuItem(
    val title: String? = null,
    val icon: Int? = null,
    val clickListener: View.OnClickListener? = null
) : BaseMenuItem()

data class MenuSeparator(val height: Int = 1, val color: Int): BaseMenuItem() // in dp

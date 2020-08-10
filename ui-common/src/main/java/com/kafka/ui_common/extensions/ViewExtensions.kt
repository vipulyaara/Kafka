package com.kafka.ui_common.extensions

import android.app.Activity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.kafka.ui_common.config.NightModeManager
import java.lang.reflect.Field

fun Toolbar?.setupToolbar(@MenuRes menu: Int, onItemClicked: (item: MenuItem?) -> Unit) {
    this?.apply {
        inflateMenu(menu)
        setOnMenuItemClickListener { item ->
            onItemClicked(item)
            true
        }
    }
}

fun Toolbar?.toggleNightMode(activity: Activity, itemId: Int) {
    this?.menuItemView(itemId)
        .springAnimation(DynamicAnimation.ROTATION)
        .doOnEnd { NightModeManager.toggleNightMode(activity) }
        .animateToFinalPosition(90f)
}


fun SpringAnimation.doOnEnd(block: () -> Unit): SpringAnimation {
    addEndListener { _, _, _, _ -> block() }
    return this
}

fun View?.springAnimation(property: DynamicAnimation.ViewProperty): SpringAnimation {
    val animation = SpringAnimation(this, property)
    val spring = SpringForce()
    spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
    spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
    animation.spring = spring
    return animation
}

/**
 * pass toolbar and menu item id, i.e. R.id.menu_refresh
 */
@Nullable
@Throws(
    IllegalAccessException::class,
    NoSuchFieldException::class
)
fun Toolbar?.menuItemView(@IdRes menuItemId: Int): View? {
    val mMenuView: Field = Toolbar::class.java.getDeclaredField("mMenuView")
    mMenuView.isAccessible = true
    val menuView: Any? = mMenuView.get(this)
    (menuView as ViewGroup).children.forEach {
        if (it.id == menuItemId) {
            return it
        }
    }
    return null
}

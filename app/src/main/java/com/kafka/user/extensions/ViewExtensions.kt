package com.kafka.user.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import java.lang.reflect.Field

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

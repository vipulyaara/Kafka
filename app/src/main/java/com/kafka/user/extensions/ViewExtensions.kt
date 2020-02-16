package com.kafka.user.extensions

import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daasuu.ei.Ease
import com.daasuu.ei.EasingInterpolator
import com.kafka.user.R

/**
 * @author Vipul Kumar; dated 27/01/19.
 */

fun View.getColor(colorRes: Int) = ContextCompat.getColor(context, colorRes)

fun View.animScaleAlphaIn(duration: Long): ViewPropertyAnimator? {
    scaleX = 1.2f
    scaleY = 1.2f
    return animate().translationY(0f)
        .scaleX(1f)
        .scaleY(1f)
        .alpha(1f).setDuration(duration)
}

fun View.animScaleAlphaOut(duration: Long): ViewPropertyAnimator? {
    scaleX = 1f
    scaleY = 1f
    return animate().translationY(-50f)
        .scaleX(1.2f)
        .scaleY(1.2f)
        .alpha(0f).setDuration(duration)
}

fun View.animateScaleOnFocus(hasFocus: Boolean, scale: Float = 1.2f) {
    if (hasFocus) {
        this.animate().scaleX(scale).scaleY(scale)
            .setInterpolator(EasingInterpolator(Ease.BACK_OUT)).duration = 420
    } else {
        this.animate().scaleX(1f).scaleY(1f)
            .setInterpolator(EasingInterpolator(Ease.BACK_OUT)).duration = 420
    }
}


/** Inflate a [ViewGroup] */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

/** Inflate a [ViewGroup] with DataBinding */
fun ViewGroup.inflateWithDataBinding(
    layoutId: Int,
    attachToRoot: Boolean = false
): ViewDataBinding {
    return DataBindingUtil
        .inflate(
            LayoutInflater.from(context),
            layoutId, this, attachToRoot
        )
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

private fun createStateForView(view: View) = ViewPaddingState(
    view.paddingLeft,
    view.paddingTop, view.paddingRight, view.paddingBottom, view.paddingStart, view.paddingEnd
)

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.transitionName() = ViewCompat.getTransitionName(this)

fun ViewGroup.getChild(tag: String?): View? {
    return findViewWithTag(tag)
}

fun View.getString(stringId: Int): String? {
    return resources.getString(stringId)
}

fun TextView.transitionColor(from: Int, to: Int) {
    ValueAnimator().apply {
        setIntValues(resources.getColor(from), resources.getColor(to))
        setEvaluator(android.animation.ArgbEvaluator())
        addUpdateListener { setTextColor(this@apply.animatedValue as Int) }
        duration = 300
        start()
    }
}

//fun RecyclerView.dividerDecoration(
//    orientation: Int = (layoutManager as LinearLayoutManager).orientation
//): DividerItemDecoration {
//    return DividerItemDecoration(context, orientation).also {
//        it.setDrawable(
//            context.getDrawable(R.drawable.list_separator) ?: ColorDrawable(
//                context?.getColorCompat(
//                    R.color.background_sep
//                ) ?: 0
//            )
//        )
//    }
//}

fun View.isVisible(condition: Boolean) {
    if (condition) visibleIfGone() else goneIfVisible()
}

fun View.visibleIfGone() {
    if (visibility == View.GONE) visibility = View.VISIBLE
}

fun View.goneIfVisible() {
    if (visibility == View.VISIBLE) visibility = View.GONE
}

val View.marginLeft: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0

val View.marginTop: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0

val View.marginRight: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0

val View.marginBottom: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0

val View.marginStart: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart ?: 0

val View.marginEnd: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd ?: 0


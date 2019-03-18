package com.kafka.user.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentTransaction
import java.lang.ref.WeakReference

class SharedElementHelper {
    val sharedElementViews = mutableMapOf<WeakReference<View>, String?>()

    fun addSharedElement(view: View, name: String? = null) {
        sharedElementViews.put(WeakReference(view), name ?: view.transitionName)
    }

    fun applyToTransaction(tx: FragmentTransaction) {
        for ((viewRef, customTransitionName) in sharedElementViews) {
            viewRef.get()?.apply {
                tx.addSharedElement(this, customTransitionName!!)
            }
        }
    }

    fun applyToIntent(activity: Activity): Bundle? {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                *sharedElementViews.map { Pair(it.key.get(), it.value) }.toList().toTypedArray()
        ).toBundle()
    }

    fun isEmpty(): Boolean = sharedElementViews.isEmpty()
}

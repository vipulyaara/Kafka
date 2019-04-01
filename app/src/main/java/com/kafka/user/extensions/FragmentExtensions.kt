package com.kafka.user.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author Vipul Kumar; dated 14/02/19.
 */
fun DialogFragment.show(fragment: Fragment) {
    show(fragment.childFragmentManager, "")
}

fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, "")
}

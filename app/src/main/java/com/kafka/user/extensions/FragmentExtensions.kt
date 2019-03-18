package com.kafka.user.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * @author Vipul Kumar; dated 14/02/19.
 */
fun DialogFragment.show(fragment: Fragment) {
    show(fragment.childFragmentManager, "")
}

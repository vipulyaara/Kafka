package com.kafka.user.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * @author Vipul Kumar; dated 19/01/19.
 */
inline fun <reified VM : ViewModel, T> T.viewModel(): Lazy<VM> where T : Fragment {
    return lazy { ViewModelProviders.of(this).get(VM::class.java) }
}

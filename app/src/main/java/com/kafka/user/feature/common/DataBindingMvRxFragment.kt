package com.kafka.user.feature.common

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewModelStore

/**
 * @author Vipul Kumar; dated 22/10/18.
 *
 * Abstract fragment for dataBinding initialization.
 * Removes a lot of boilerplate when working with dataBinding.
 */
abstract class DataBindingMvRxFragment<Binding : ViewDataBinding>(
    layoutRes: Int
) : DataBindingFragment<Binding>(layoutRes), MvRxView {
    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }

    override fun onStart() {
        super.onStart()
        // This ensures that invalidate() is called for static screens that don't
        // subscribe to a ViewModel.
        postInvalidate()
    }
}

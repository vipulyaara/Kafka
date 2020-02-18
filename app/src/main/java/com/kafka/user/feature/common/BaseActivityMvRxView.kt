package com.kafka.user.feature.common

import android.os.Bundle
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewId
import com.kafka.user.feature.common.BaseActivity

/**
 * Base Activity class which supports LifecycleOwner and Dagger injection.
 */
abstract class BaseActivityMvRxView : BaseActivity(), MvRxView {

    private val mvrxViewIdProperty = MvRxViewId()
    final override val mvrxViewId: String by mvrxViewIdProperty

    override fun onCreate(savedInstanceState: Bundle?) {
        mvrxViewIdProperty.restoreFrom(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewIdProperty.saveTo(outState)
    }

    override fun onStart() {
        super.onStart()
        // This ensures that invalidate() is called for static screens that don't
        // subscribe to a ViewModel.
        postInvalidate()
    }
}

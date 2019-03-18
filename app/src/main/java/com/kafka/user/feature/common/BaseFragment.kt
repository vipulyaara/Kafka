package com.kafka.user.feature.common

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Vipul Kumar; dated 22/10/18.
 */

abstract class BaseFragment : Fragment() {
    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        toolbar = (activity as BaseActivity).toolbar
        super.onCreate(savedInstanceState)
    }
}

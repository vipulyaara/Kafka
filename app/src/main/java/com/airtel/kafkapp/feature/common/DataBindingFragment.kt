package com.airtel.kafkapp.feature.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.data.config.logging.Logger
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 22/10/18.
 *
 * Abstract fragment for dataBinding initialization.
 * Removes a lot of boilerplate when working with dataBinding.
 */
abstract class DataBindingFragment<Binding : ViewDataBinding>(
    private val layoutRes: Int
) : BaseFragment() {

    protected val logger: Logger by kodeinInstance.instance()
    protected lateinit var binding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, layoutRes, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }
}

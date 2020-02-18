package com.kafka.user.feature.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import javax.inject.Inject

/**
 * Created by VipulKumar on 21/09/18.
 */
abstract class BaseDataBindingFragment<Binding : ViewDataBinding>(
    private val layoutRes: Int
) : BaseMvFragment() {

    private var _binding: Binding? = null
    val binding by lazy { binding() }

    private fun binding() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = DataBindingUtil
                .inflate(inflater, layoutRes, container, false)
            _binding?.lifecycleOwner = this
        }
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

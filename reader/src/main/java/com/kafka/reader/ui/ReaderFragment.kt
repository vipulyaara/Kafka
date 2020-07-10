package com.kafka.reader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kafka.reader.R
import com.kafka.ui_common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReaderFragment : BaseFragment() {
    private val viewModel: ReaderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startActivity(viewModel.getPdfIntent())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }
}

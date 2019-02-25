package com.airtel.kafkapp

import android.os.Bundle
import com.airtel.kafkapp.feature.common.BaseActivity

/**
 * @author Vipul Kumar; dated 20/02/19.
 */
class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_item_detail)
    }
}

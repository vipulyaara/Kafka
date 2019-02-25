package com.airtel.kafkapp.feature.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.airtel.kafkapp.config.NightModeManager

/**
 * @author Vipul Kumar; dated 22/10/18.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
        set(value) {
//            setSupportActionBar(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NightModeManager.setNightModeFromSharedPrefs(this)
    }
}

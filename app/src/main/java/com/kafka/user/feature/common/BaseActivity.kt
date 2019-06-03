package com.kafka.user.feature.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kafka.user.config.NightModeManager

/**
 * @author Vipul Kumar; dated 22/10/18.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NightModeManager.setNightModeFromSharedPrefs(this)
    }
}

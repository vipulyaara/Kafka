package com.kafka.user.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw

/**
 * @author Vipul Kumar; dated 2019-08-15.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    private var postponedTransition = false

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun postponeEnterTransition() {
        super.postponeEnterTransition()
        postponedTransition = true
    }

    override fun startPostponedEnterTransition() {
        postponedTransition = false
        super.startPostponedEnterTransition()
    }

    fun scheduleStartPostponedTransitions() {
        if (postponedTransition) {
            window.decorView.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    open fun handleIntent(intent: Intent) {}

    override fun finishAfterTransition() {
        val resultData = Intent()
        val result = onPopulateResultIntent(resultData)
        setResult(result, resultData)

        super.finishAfterTransition()
    }

    open fun onPopulateResultIntent(intent: Intent): Int {
        return Activity.RESULT_OK
    }
}

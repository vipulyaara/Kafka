package org.kafka.analytics

import android.app.Activity

interface AppReviewManager {
    fun showReviewDialog(activity: Activity)
    suspend fun incrementItemOpenCount()
    val totalItemOpens: Int
}

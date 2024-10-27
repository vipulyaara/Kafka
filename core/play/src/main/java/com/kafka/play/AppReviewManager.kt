package com.kafka.play

import android.app.Activity
import android.app.Application
import com.google.android.play.core.review.ReviewManagerFactory
import com.kafka.analytics.providers.Analytics
import com.kafka.base.ApplicationScope
import com.kafka.base.errorLog
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class AppReviewManagerImpl(
    context: Application,
    private val analytics: Analytics,
) : AppReviewManager {
    private val manager = ReviewManagerFactory.create(context)

    override fun showReviewDialog(activity: Any?) {
        manager.requestReviewFlow().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                analytics.log { showAppReviewDialog() }
                if (activity is Activity) {
                    manager.launchReviewFlow(activity, task.result)
                }
            } else {
                errorLog(task.exception) { "Error while generating review info" }
            }
        }
    }
}

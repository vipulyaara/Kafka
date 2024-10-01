package org.kafka.play

import android.app.Activity
import android.app.Application
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.CoroutineScope
import org.kafka.analytics.logger.Analytics
import org.kafka.base.ApplicationScope
import org.kafka.base.ProcessLifetime
import org.kafka.base.errorLog
import javax.inject.Inject

interface AppReviewManager {
    fun showReviewDialog(activity: Activity)
}

@ApplicationScope
class AppReviewManagerImpl @Inject constructor(
    context: Application,
    @ProcessLifetime private val coroutineScope: CoroutineScope,
    private val analytics: Analytics,
) : AppReviewManager {
    private val manager = ReviewManagerFactory.create(context)

    override fun showReviewDialog(activity: Activity) {
        manager.requestReviewFlow().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                analytics.log { showAppReviewDialog() }
                manager.launchReviewFlow(activity, task.result)
            } else {
                errorLog(task.exception) { "Error while generating review info" }
            }
        }
    }
}

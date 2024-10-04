package org.kafka.play

import me.tatarka.inject.annotations.Provides
import org.kafka.base.ApplicationScope

actual interface PlayStoreComponent {
    @ApplicationScope
    @Provides
    fun provideAppReviewManager(): AppReviewManager = object : AppReviewManager {
        override fun showReviewDialog(activity: Any?) {
            // todo: kmp implement
        }
    }
}

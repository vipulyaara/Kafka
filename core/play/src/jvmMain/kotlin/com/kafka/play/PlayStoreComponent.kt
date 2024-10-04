package com.kafka.play

import me.tatarka.inject.annotations.Provides
import com.kafka.base.ApplicationScope
import com.kafka.play.AppReviewManager

actual interface PlayStoreComponent {
    @ApplicationScope
    @Provides
    fun provideAppReviewManager(): AppReviewManager = object : AppReviewManager {
        override fun showReviewDialog(activity: Any?) {
            // todo: kmp implement
        }
    }
}

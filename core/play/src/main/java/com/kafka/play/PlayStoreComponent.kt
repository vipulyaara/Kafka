package com.kafka.play

import me.tatarka.inject.annotations.Provides
import com.kafka.base.ApplicationScope
import com.kafka.play.AppReviewManager

actual interface PlayStoreComponent {
    @ApplicationScope
    @Provides
    fun provideAppReviewManager(bind: AppReviewManagerImpl): AppReviewManager = bind
}

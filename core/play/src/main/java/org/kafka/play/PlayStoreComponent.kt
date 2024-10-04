package org.kafka.play

import me.tatarka.inject.annotations.Provides
import com.kafka.base.ApplicationScope

actual interface PlayStoreComponent {
    @ApplicationScope
    @Provides
    fun provideAppReviewManager(bind: AppReviewManagerImpl): AppReviewManager = bind
}

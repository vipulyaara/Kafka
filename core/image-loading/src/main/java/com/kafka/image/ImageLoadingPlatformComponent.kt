package com.kafka.image

import android.app.Application
import coil3.PlatformContext
import me.tatarka.inject.annotations.Provides

actual interface ImageLoadingPlatformComponent {

    @Provides
    fun providePlatformContext(application: Application): PlatformContext = application
}

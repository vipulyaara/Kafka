package com.kafka.user.injection

import android.app.Activity
import com.kafka.remote.config.RemoteConfig
import com.kafka.user.home.MainScreen
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.kafka.base.ActivityScope

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
) {
    abstract val mainScreen: MainScreen
    abstract val remoteConfig: RemoteConfig

    companion object
}

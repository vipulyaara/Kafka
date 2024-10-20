package com.kafka.shared.injection

import android.app.Activity
import com.kafka.base.ActivityScope
import com.kafka.remote.config.RemoteConfig
import com.kafka.shared.home.MainScreen
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

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

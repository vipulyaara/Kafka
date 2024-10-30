package com.kafka.shared

import android.app.Activity
import com.kafka.base.ActivityScope
import com.kafka.remote.config.RemoteConfig
import com.kafka.root.RootContent
import com.kafka.root.SharedUiComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
) : SharedUiComponent {
    abstract val remoteConfig: RemoteConfig
    abstract val rootContent: RootContent

    companion object
}

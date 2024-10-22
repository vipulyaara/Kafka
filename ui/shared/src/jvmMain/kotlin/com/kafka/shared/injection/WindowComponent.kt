package com.kafka.shared.injection

import com.kafka.base.ActivityScope
import com.kafka.base.AppInitializer
import com.kafka.shared.home.MainScreen
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) {
    abstract val mainScreen: MainScreen
    abstract val appInitializers: Set<AppInitializer>

    companion object
}

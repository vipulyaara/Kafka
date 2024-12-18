package com.kafka.desktop

import com.kafka.base.ActivityScope
import com.kafka.base.AppInitializer
import com.kafka.desktop.main.HomeNavigation
import com.kafka.shared.DesktopApplicationComponent
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) {
    abstract val mainScreen: HomeNavigation
    abstract val appInitializers: Set<AppInitializer>

    companion object
}

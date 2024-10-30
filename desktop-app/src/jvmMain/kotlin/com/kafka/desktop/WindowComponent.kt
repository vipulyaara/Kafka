package com.kafka.desktop

import com.kafka.base.ActivityScope
import com.kafka.base.AppInitializer
import com.kafka.root.RootContent
import com.kafka.root.SharedUiComponent
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) : SharedUiComponent {
    abstract val appInitializers: Set<AppInitializer>
    abstract val rootContent: RootContent

    companion object
}

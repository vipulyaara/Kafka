package com.kafka.kms.main

import com.kafka.base.ActivityScope
import com.kafka.base.AppInitializer
import com.kafka.kms.ui.KmsHomepage
import com.kafka.root.SharedUiComponent
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class KmsWindowComponent(
    @Component val applicationComponent: KmsApplicationComponent,
) : SharedUiComponent {
    abstract val appInitializers: Set<AppInitializer>
    abstract val kmsHomepage: KmsHomepage

    companion object
} 
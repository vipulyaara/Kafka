package com.kafka.root

import com.kafka.base.ActivityScope
import me.tatarka.inject.annotations.Provides

interface SharedUiComponent {
    @Provides
    @ActivityScope
    fun bindRootContent(impl: DefaultRootContent): RootContent = impl
}

package com.kafka.navigation

import me.tatarka.inject.annotations.Provides
import com.kafka.base.ApplicationScope

interface NavigationModule {

    @Provides
    @ApplicationScope
    fun navigator(bind: NavigatorImpl): Navigator = bind
}

package org.kafka.navigation

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.kafka.base.ApplicationScope

@Component
@ApplicationScope
interface NavigationModule {

    @Provides
    @ApplicationScope
    fun navigator(bind: NavigatorImpl): Navigator = bind
}

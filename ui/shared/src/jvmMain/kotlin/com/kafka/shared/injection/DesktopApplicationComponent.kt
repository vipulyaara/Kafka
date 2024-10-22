package com.kafka.shared.injection

import com.kafka.base.ApplicationScope
import com.kafka.data.prefs.PreferencesStore
import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class DesktopApplicationComponent : SharedApplicationComponent {
    abstract val preferencesStore: PreferencesStore

    companion object
}

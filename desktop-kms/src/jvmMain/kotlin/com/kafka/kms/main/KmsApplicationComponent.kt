package com.kafka.kms.main

import com.kafka.base.ApplicationScope
import com.kafka.data.prefs.PreferencesStore
import com.kafka.shared.common.injection.SharedApplicationComponent
import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class KmsApplicationComponent : SharedApplicationComponent {
    abstract val preferencesStore: PreferencesStore

    companion object
} 
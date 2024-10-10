package com.kafka.data.prefs

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface PreferenceStoreComponent {
    @ApplicationScope
    @Provides
    fun providePreferencesStore(): PreferencesStore {
        return PreferencesStore(createDataStore { dataStoreFileName })
    }
}

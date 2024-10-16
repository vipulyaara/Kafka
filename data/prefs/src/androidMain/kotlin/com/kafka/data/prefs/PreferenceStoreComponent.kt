package com.kafka.data.prefs

import android.app.Application
import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface PreferenceStoreComponent {
    @ApplicationScope
    @Provides
    fun providePreferencesStore(context: Application): PreferencesStore {
        val dataStore = createDataStore(
            producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
        )

        return PreferencesStore(dataStore)
    }
}

@file:OptIn(ExperimentalForeignApi::class)

package com.kafka.data.prefs

import com.kafka.base.ApplicationScope
import kotlinx.cinterop.ExperimentalForeignApi
import me.tatarka.inject.annotations.Provides
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual interface PreferenceStoreComponent {
    @ApplicationScope
    @Provides
    fun providePreferencesStore(): PreferencesStore {
        val dataStore = createDataStore(
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/$dataStoreFileName"
            }
        )

        return PreferencesStore(dataStore)
    }
}

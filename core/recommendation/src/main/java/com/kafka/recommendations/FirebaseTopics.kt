package com.kafka.recommendations

import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.firebase.messaging.FirebaseMessaging
import com.kafka.data.prefs.PreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.kafka.base.ProcessLifetime
import timber.log.Timber
import javax.inject.Inject

interface FirebaseTopics {
    fun subscribeToTopic(topic: String)
    fun unsubscribeFromTopic(topic: String)
    val topics: Flow<List<String>>
}

class FirebaseTopicsImpl @Inject constructor(
    @ProcessLifetime private val processScope: CoroutineScope,
    private val firebaseMessaging: FirebaseMessaging,
    private val preferencesStore: PreferencesStore
) : FirebaseTopics {
    private val topicsFlow =
        preferencesStore.data.map { it[topicPreferenceKey] ?: emptySet() }

    override fun subscribeToTopic(topic: String) {
        processScope.launch {
            if (topicsFlow.firstOrNull()?.contains(topic) != true) {
                preferencesStore.save(topicPreferenceKey, topicsFlow.first() + topic)

                try {
                    firebaseMessaging.subscribeToTopic(topic).await()
                } catch (e: Exception) {
                    Timber.e(e, "Failed to subscribe to topic $topic")
                }
            }
        }
    }

    override fun unsubscribeFromTopic(topic: String) {
        processScope.launch {
            preferencesStore.save(topicPreferenceKey, topicsFlow.first() - topic)

            try {
                firebaseMessaging.unsubscribeFromTopic(topic).await()
            } catch (e: Exception) {
                Timber.e(e, "Failed to unsubscribe topic $topic")
            }
        }
    }

    override val topics: Flow<List<String>>
        get() = topicsFlow.map { it.toList() }
}

private val topicPreferenceKey = stringSetPreferencesKey("firebase_topics")

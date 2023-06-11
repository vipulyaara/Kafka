package com.kafka.recommendations

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.kafka.base.ProcessLifetime
import javax.inject.Inject

interface FirebaseTopics {
    fun subscribeToTopic(topic: String)
    fun unsubscribeFromTopic(topic: String)
    fun observeTopics(): Flow<List<String>>
}

class FirebaseTopicsImpl @Inject constructor(
    @ProcessLifetime private val processScope: CoroutineScope,
    private val firebaseMessaging: FirebaseMessaging
) : FirebaseTopics {
    override fun subscribeToTopic(topic: String) {
        processScope.launch {
            firebaseMessaging.subscribeToTopic(topic).await()
        }
    }

    override fun unsubscribeFromTopic(topic: String) {
        processScope.launch {
            firebaseMessaging.unsubscribeFromTopic(topic).await()
        }
    }

    override fun observeTopics(): Flow<List<String>> {
        return flowOf()
    }
}

private val topicPreferenceKey = stringPreferencesKey("firebase_topics")

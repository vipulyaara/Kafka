package com.kafka.recommendations

interface FirebaseTopics {
    fun subscribeToTopic()
    fun unsubscribeFromTopic()
    fun observeTopics(): List<String>
}


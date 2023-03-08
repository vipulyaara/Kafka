package com.kafka.data.feature.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Reusable
import javax.inject.Inject

@Reusable
class FirestoreGraph @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    val recentItemsCollection
        get() = getRecentItemsCollection(auth.currentUser!!.uid)

    fun getRecentItemsCollection(id: String) = firestore.collection("recent_items")
        .document(id)
        .collection("items")
}

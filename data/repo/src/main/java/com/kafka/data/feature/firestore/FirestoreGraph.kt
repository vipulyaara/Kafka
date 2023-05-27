package com.kafka.data.feature.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Reusable
import dev.gitlive.firebase.firestore.CollectionReference
import javax.inject.Inject
import dev.gitlive.firebase.firestore.FirebaseFirestore as FirebaseFirestoreKt

@Reusable
class FirestoreGraph @Inject constructor(
    private val firestoreKt: FirebaseFirestoreKt,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    val recentItemsCollection
        get() = getRecentItemsCollection(auth.currentUser!!.uid)

    fun getRecentItemsCollection(id: String) = firestore
        .collection("recent_items")
        .document(id)
        .collection("items")

    val homepageCollection: CollectionReference
        get() = firestoreKt
            .collection("homepage-collection")

    val feedbackCollection: CollectionReference
        get() = firestoreKt.collection("feedback")

    fun getFavoritesCollection(id: String) = firestore
        .collection("favorites")
        .document(id)
        .collection("items")
}

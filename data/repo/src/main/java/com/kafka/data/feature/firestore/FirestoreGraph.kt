package com.kafka.data.feature.firestore

import com.google.firebase.auth.FirebaseAuth
import dagger.Reusable
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

@Reusable
class FirestoreGraph @Inject constructor(
    private val firestoreKt: FirebaseFirestore,
    private val firestore: com.google.firebase.firestore.FirebaseFirestore,
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
            .collection("homepage")
            .document("collection_items")
            .collection("items")

    val feedbackCollection: CollectionReference
        get() = firestoreKt.collection("feedback")

    val homepageBanners: CollectionReference
        get() = firestoreKt
            .collection("homepage")
            .document("banners")
            .collection("items")

    fun getFavoritesCollection(id: String) = firestore
        .collection("favorites")
        .document(id)
        .collection("items")
}

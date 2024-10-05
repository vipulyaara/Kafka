package com.kafka.data.feature.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.kafka.base.ApplicationScope
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentReference
import javax.inject.Inject
import dev.gitlive.firebase.firestore.FirebaseFirestore as FirebaseFirestoreKt

@ApplicationScope
class FirestoreGraph @Inject constructor(
    private val firestoreKt: FirebaseFirestoreKt,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
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

    val appUpdateConfig: DocumentReference
        get() = firestoreKt
            .collection("app_config")
            .document("app_update")

    val feedbackCollection: CollectionReference
        get() = firestoreKt.collection("feedback")

    val summaryCollection: CollectionReference
        get() = firestoreKt.collection("summary")

    fun getListCollection(uid: String, listId: String) = firestore
        .collection("favorites")
        .document(uid)
        .collection(listId)

    fun getRecommendationCollection() = firestore.collection("user_recommendations")

    fun getRecommendationCollection(uid: String, recommendationId: String) =
        getRecommendationCollection()
            .document(uid)
            .collection(recommendationId)

    fun getDownloadsCollection(id: String) = firestore
        .collection("downloads")
        .document(id)
        .collection("items")
}

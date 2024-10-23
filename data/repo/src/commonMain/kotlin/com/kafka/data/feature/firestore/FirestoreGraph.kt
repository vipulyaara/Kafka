package com.kafka.data.feature.firestore

import com.kafka.base.ApplicationScope
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentReference
import javax.inject.Inject
import dev.gitlive.firebase.firestore.FirebaseFirestore as FirebaseFirestoreKt

@ApplicationScope
class FirestoreGraph @Inject constructor(
    private val firestoreKt: FirebaseFirestoreKt
) {
    val homepageCollection: CollectionReference
        get() = firestoreKt
            .collection("homepage-collection-debug") //todo

    val appUpdateConfig: DocumentReference
        get() = firestoreKt
            .collection("app_config")
            .document("app_update")

    val appMessageConfig: DocumentReference
        get() = firestoreKt
            .collection("app_config")
            .document("app_message")

    val summaryCollection: CollectionReference
        get() = firestoreKt.collection("summary")

    fun getListCollection(uid: String, listId: String) = firestoreKt
        .collection("favorites")
        .document(uid)
        .collection(listId)
}

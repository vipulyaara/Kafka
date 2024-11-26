package com.kafka.data.feature.firestore

import com.kafka.base.ApplicationScope
import com.kafka.data.entities.BookshelfDefaults
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class FirestoreGraph(private val firestore: FirebaseFirestore) {
    val homepageCollection: CollectionReference
        get() = firestore
            .collection("homepage-collection-debug") //todo

    val appUpdateConfig: DocumentReference
        get() = firestore
            .collection("app_config")
            .document("app_update")

    val appMessageConfig: DocumentReference
        get() = firestore
            .collection("app_config")
            .document("app_message")

    val summaryCollection: CollectionReference
        get() = firestore.collection("summary")

    val reportsCollection: CollectionReference
        get() = firestore.collection("reports")

    fun listCollection(uid: String) = firestore
        .collection("users")
        .document(uid)
        .collection("lists")

    fun listItemsCollection(uid: String, listId: String) = listCollection(uid)
        .document(listId)
        .collection("items")

    fun readingListCollection(uid: String) =
        listItemsCollection(uid, BookshelfDefaults.reading.id)

    fun highlightCollection() = firestore
        .collection("highlights")

    fun batch() = firestore.batch()
}

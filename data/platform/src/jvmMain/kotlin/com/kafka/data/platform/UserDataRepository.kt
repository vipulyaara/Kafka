package com.kafka.data.platform

import com.kafka.base.ApplicationScope
import dev.gitlive.firebase.auth.FirebaseAuth
import javax.inject.Inject

@ApplicationScope
actual class UserDataRepository @Inject constructor(private val auth: FirebaseAuth) {
    actual suspend fun getUserData(): UserData {
        // todo: kmp implement
        return UserData(auth.currentUser?.uid, "IN")
    }
}

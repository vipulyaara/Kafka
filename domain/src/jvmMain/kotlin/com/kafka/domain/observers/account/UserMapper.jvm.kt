package com.kafka.domain.observers.account

import com.kafka.data.entities.User
import dev.gitlive.firebase.auth.FirebaseUser

actual fun FirebaseUser.asUser(): User {
    return User(
        id = uid,
        email = "NA",
        displayName = "NA",
        imageUrl = "NA",
        anonymous = isAnonymous,
    )
}
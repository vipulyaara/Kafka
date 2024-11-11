package com.kafka.domain.observers.account

import com.kafka.data.entities.User
import dev.gitlive.firebase.auth.FirebaseUser

expect fun FirebaseUser.asUser(): User

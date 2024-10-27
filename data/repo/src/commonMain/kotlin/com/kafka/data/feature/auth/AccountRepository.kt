package com.kafka.data.feature.auth

import com.kafka.base.ApplicationScope
import com.kafka.base.debug
import com.kafka.data.entities.User
import com.kafka.data.feature.Supabase
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

/**
 * Account management has complex rules specifically due to anonymous authentication.
 * The app lets users sign in anonymously and then link their account to an email/password.
 * Anonymous users can keep their data when they link their account.
 * */
@ApplicationScope
@Inject
class AccountRepository(private val supabase: Supabase) {
    val auth = supabase.auth

    val currentUser
        get() = auth.currentUserOrNull()!!

    val currentUserOrNull
        get() = auth.currentUserOrNull()

    suspend fun signInAnonymously() = auth.signInAnonymously()

    suspend fun signUp(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String) =
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

    suspend fun resetPassword(email: String) {
        auth.resetPasswordForEmail(email)
    }

    fun observeCurrentUser(): Flow<User> {
        return supabase.auth.sessionStatus.map { session ->
            when (session) {
                is SessionStatus.Authenticated -> {
                    debug { "Received new authenticated session." }
                    supabase.auth.currentUserOrNull()!!
                        .asUser(session.source is SessionSource.AnonymousSignIn)
                }

                SessionStatus.Initializing -> null
                is SessionStatus.NotAuthenticated -> null
                is SessionStatus.RefreshFailure -> null
            }
        }.filterNotNull()
    }

    private fun UserInfo.asUser(anonymous: Boolean) = User(
        id = id,
        displayName = "",
        email = email,
        imageUrl = "",
        anonymous = anonymous,
    )

    suspend fun signOut() {
        auth.signOut()
    }
}

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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Account management has complex rules specifically due to anonymous authentication.
 * The app lets users sign in anonymously and then link their account to an email/password.
 * Anonymous users can keep their data when they link their account.
 * */
@ApplicationScope
class AccountRepository @Inject constructor(
    private val supabase: Supabase,
) {
    val auth = supabase.auth

    val currentUser
        get() = auth.currentUserOrNull()

    val isUserLoggedIn
        get() = currentUser != null

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

    fun observeCurrentUserOrNull(): Flow<User?> {
        return supabase.auth.sessionStatus.map {
            when (it) {
                is SessionStatus.Authenticated -> {
                    debug { "Received new authenticated session." }
                    when (it.source) {
                        SessionSource.External,
                        SessionSource.Storage,
                        SessionSource.Unknown,
                        is SessionSource.Refresh,
                        is SessionSource.SignIn,
                        is SessionSource.SignUp,
                        is SessionSource.UserChanged,
                        is SessionSource.UserIdentitiesChanged -> {
                            supabase.auth.currentUserOrNull()!!.asUser(false)
                        }

                        SessionSource.AnonymousSignIn -> {
                            supabase.auth.currentUserOrNull()!!.asUser(true)
                        }
                    }
                }

                SessionStatus.Initializing,
                is SessionStatus.RefreshFailure,
                is SessionStatus.NotAuthenticated -> {
                    null
                }
            }
        }
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

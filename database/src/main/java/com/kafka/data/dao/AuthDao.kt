package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AuthDao : EntityDao<User> {
    @Query("SELECT * FROM user")
    abstract fun observeUser(): Flow<User?>

    @Query("SELECT * FROM user")
    abstract fun getUser(): User?

    @Query("DELETE FROM user")
    abstract suspend fun delete()

    @Query("DELETE FROM user")
    abstract suspend fun deleteAll()
}

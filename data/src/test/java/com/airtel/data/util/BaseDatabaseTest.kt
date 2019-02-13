package com.airtel.data.util

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.airtel.data.data.db.MiddlewareDb
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
abstract class BaseDatabaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    internal lateinit var db: MiddlewareDb
        private set

    @Before
    open fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MiddlewareDb::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    @Throws(IOException::class)
    open fun closeDb() {
        db.close()
    }
}

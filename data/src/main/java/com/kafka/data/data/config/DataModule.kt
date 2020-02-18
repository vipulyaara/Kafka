package com.kafka.data.data.config

import com.kafka.data.util.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class DataModule {
    @Provides
    fun provideAppCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )
}
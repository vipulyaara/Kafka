package com.kafka.data.di

import com.data.base.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module(includes = [DatabaseDaoModule::class, ServiceModule::class])
class DataModule {
    @Provides
    fun provideAppCoroutineDispatchers() =
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
}

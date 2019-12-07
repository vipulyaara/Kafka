package com.kafka.data.data.config

import android.app.Application
import android.content.Context
import com.kafka.data.data.api.ArchiveService
import com.kafka.data.data.api.RetrofitProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {
    @Singleton
    @Provides
    fun provideRekhtaService(context: Context) = RetrofitProvider
        .provideDefaultRetrofit(context)
        .create(ArchiveService::class.java)
}
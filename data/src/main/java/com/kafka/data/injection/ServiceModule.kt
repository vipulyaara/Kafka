//package com.kafka.data.injection
//
//import android.app.Application
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import com.kafka.data.api.ArchiveService
//import com.kafka.data.api.interceptor.AcceptDialogInterceptor
//import com.kafka.data.api.interceptor.RewriteCachesInterceptor
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.json.Json
//import okhttp3.Cache
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
//import okhttp3.Protocol
//import okhttp3.logging.HttpLoggingInterceptor
//import org.threeten.bp.Duration
//import retrofit2.Retrofit
//import java.util.concurrent.TimeUnit
//import javax.inject.Named
//import javax.inject.Singleton
//
//const val baseUrl = "https://archive.org/"
//
//private val json = Json {
//    ignoreUnknownKeys = true
//    isLenient = true
//}
//
//@InstallIn(SingletonComponent::class)
//@Module
//class ServiceModule {
//
//    @Provides
//    fun provideService(
//        retrofit: Retrofit
//    ): ArchiveService {
//        return retrofit.create(ArchiveService::class.java)
//    }
//
//    @ImageLoading
//    @Provides
//    fun provideOkHttpClientCoil(): OkHttpClient {
//        val builder = OkHttpClient.Builder().apply {
//            readTimeout(60, TimeUnit.SECONDS)
//            connectTimeout(60, TimeUnit.SECONDS)
//            addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//        }
//
//        return builder.build()
//    }
//
//    @Provides
//    fun provideOkHttpClientBuilder(
//        acceptDialogInterceptor: AcceptDialogInterceptor,
//        httpLoggingInterceptor: HttpLoggingInterceptor,
//        rewriteCachesInterceptor: RewriteCachesInterceptor
//    ): OkHttpClient {
//        val builder = OkHttpClient.Builder().apply {
//            readTimeout(60, TimeUnit.SECONDS)
//            connectTimeout(60, TimeUnit.SECONDS)
//            protocols(listOf(Protocol.HTTP_1_1))
//            addInterceptor(acceptDialogInterceptor)
//            addInterceptor(httpLoggingInterceptor)
//            addInterceptor(rewriteCachesInterceptor)
//        }
//
//        return builder.build()
//    }
//
//    @Provides
//    @Singleton
//    fun okHttpCache(app: Application) = Cache(app.cacheDir, (10 * 1024 * 1024).toLong())
//
//    @Provides
//    @Singleton
//    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BASIC
//        return interceptor
//    }
//
//    @Provides
//    @Named("downloader")
//    fun downloaderOkHttp(
//        acceptDialogInterceptor: AcceptDialogInterceptor,
//        rewriteCachesInterceptor: RewriteCachesInterceptor,
//        httpLoggingInterceptor: HttpLoggingInterceptor,
//        cache: Cache,
//    ) = OkHttpClient.Builder()
//        .readTimeout(Duration.ofMinutes(3).toMillis(), TimeUnit.MILLISECONDS)
//        .writeTimeout(Duration.ofMinutes(3).toMillis(), TimeUnit.MILLISECONDS)
//        .addInterceptor(acceptDialogInterceptor)
//        .addInterceptor(httpLoggingInterceptor)
//        .addInterceptor(rewriteCachesInterceptor)
//        .build()
//
//    @ExperimentalSerializationApi
//    @Provides
//    fun provideDefaultRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit {
//        val contentType = "application/json".toMediaType()
//        return Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .client(okHttpClient)
//            .addConverterFactory(json.asConverterFactory(contentType))
//            .build()
//    }
//
//
//    private fun getBaseBuilder(cache: Cache): OkHttpClient.Builder {
//        return OkHttpClient.Builder()
//            .cache(cache)
//            .readTimeout(Config.API_TIMEOUT, TimeUnit.MILLISECONDS)
//            .writeTimeout(Config.API_TIMEOUT, TimeUnit.MILLISECONDS)
//            .retryOnConnectionFailure(true)
//    }
//}
//
//
//clas

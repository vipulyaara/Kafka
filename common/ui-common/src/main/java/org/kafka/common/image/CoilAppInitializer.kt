package org.kafka.common.image

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.kafka.data.AppInitializer
import com.kafka.data.injection.ImageLoading
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class CoilAppInitializer @Inject constructor(
    @ImageLoading private val okHttpClient: OkHttpClient,
    @ProcessLifetime private val processScope: CoroutineScope
) : AppInitializer {

    override fun init(application: Application) {
        processScope.launch {
            val coilOkHttpClient = okHttpClient.newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                })
                .build()

            Logger.getLogger(OkHttpClient::class.java.name).level = Level.OFF
            Coil.setImageLoader {
                ImageLoader.Builder(application)
                    .okHttpClient(coilOkHttpClient)
                    .build()
            }
        }
    }
}

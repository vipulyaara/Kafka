package com.airtel.kafkapp

import android.app.Application
import com.airtel.data.config.di.appModule
import com.airtel.data.config.di.bookDetailModule
import com.airtel.data.config.kodeinInstance
import com.airtel.kafka.Kafka
import com.airtel.kafka.config.di.visionModule
import com.airtel.kafkapp.config.EpoxyInitializer
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
class KafkaApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidModule(this@KafkaApplication))
        import(appModule)
        import(bookDetailModule)
        import(visionModule)
    }

    override fun onCreate() {
        super.onCreate()
        kodeinInstance = kodein
        initVision()
        EpoxyInitializer().init(this)
    }

    private fun initVision() {
        val configuration = Kafka.configuration {
            appId = "my_app_id"
            debuggable = true
        }
        Kafka.init(this, configuration)
    }
}

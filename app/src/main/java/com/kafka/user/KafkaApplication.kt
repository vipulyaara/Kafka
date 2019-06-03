package com.kafka.user

import android.app.Application
import com.kafka.data.data.config.di.appModule
import com.kafka.data.data.config.di.dataModule
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.config.logging.TimberInitializer
import com.kafka.data.data.config.logging.TimberLogger
import com.kafka.user.config.EpoxyInitializer
import com.kafka.user.config.StethoInitializer
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
class KafkaApplication : Application(), KodeinAware {

    /** setting up kodein dependencies */
    override val kodein: Kodein = Kodein.lazy {
        import(androidModule(this@KafkaApplication))
        import(appModule)
        import(dataModule)
    }

    override fun onCreate() {
        super.onCreate()
        kodeinInstance = kodein
        EpoxyInitializer().init(this)
        StethoInitializer().init(this)
        TimberInitializer(TimberLogger()).init(this)
    }
}

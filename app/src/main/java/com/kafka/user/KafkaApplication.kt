package com.kafka.user

import android.app.Application
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.kafka.data.data.config.di.appModule
import com.kafka.data.data.config.di.dataModule
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.config.logging.TimberInitializer
import com.kafka.data.data.config.logging.TimberLogger
import com.kafka.user.config.EpoxyInitializer
import com.kafka.user.config.StethoInitializer
import com.kafka.user.player.Player
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

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

        Player.bindService(this)

        updateSSL(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        Player.unbindService(this)
    }

    private fun updateSSL(application: Application) {
        try {
            ProviderInstaller.installIfNeeded(application)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
    }
}

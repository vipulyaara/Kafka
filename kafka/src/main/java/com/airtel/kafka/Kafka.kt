package com.airtel.kafka

import android.app.Application
import com.airtel.data.config.initializers.AppInitializers
import com.airtel.data.config.kodeinInstance
import com.airtel.data.data.sharedPrefs.KEY_USER_TOKEN
import com.airtel.data.data.sharedPrefs.KEY_USER_UID
import com.airtel.data.data.sharedPrefs.UserPreferenceManager
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
object Kafka {
    lateinit var application: Application
    private val userPreferenceManager: UserPreferenceManager by kodeinInstance.instance()
    private val appInitializers: AppInitializers by kodeinInstance.instance()

    fun init(application: Application, configuration: Configuration) {
        this.application = application
        appInitializers.init(application)
        userPreferenceManager.putString(KEY_USER_UID, "odZconnsqGMNLKXrI0")
        userPreferenceManager.putString(KEY_USER_TOKEN, "NdqbkenyGs5l15qOv7CE8AnqU3k=")
    }

    class Configuration {
        var appId: String = ""
        var debuggable = false
    }

    fun configuration(config: Configuration.() -> Unit): Configuration {
        return Configuration().apply {
            config.invoke(this)
        }
    }

    fun getBookDetail(contentId: String) = BookService.getBookDetail(contentId)

    fun getSuggestedContent(authorLastName: String) = BookService.getBooksByAuthor(authorLastName)
}

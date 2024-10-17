package com.kafka.data.platform.device

import android.app.Application
import android.content.Context
import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface PlatformCountryComponent {
    @ApplicationScope
    @Provides
    fun providePlatformCountry(application: Application): PlatformCountry {
        val telephonyManager = application.getSystemService(Context.TELEPHONY_SERVICE)
                as android.telephony.TelephonyManager

        val country = telephonyManager.networkCountryIso.takeIf { it.isNotBlank() }

        return PlatformCountry(country)
    }
}

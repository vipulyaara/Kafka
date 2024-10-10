package com.kafka.data.platform.device

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

actual interface PlatformCountryComponent {
  @ApplicationScope
  @Provides
  fun providePlatformCountry() = PlatformCountry(null)
}

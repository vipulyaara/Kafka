package com.kafka.data.platform

import com.kafka.data.platform.app.AppVersionComponent
import com.kafka.data.platform.device.PlatformCountryComponent

actual interface DataPlatformComponent : PlatformCountryComponent, AppVersionComponent

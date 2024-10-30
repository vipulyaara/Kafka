package com.kafka.shared

import com.kafka.base.ApplicationScope
import com.kafka.shared.common.injection.SharedApplicationComponent
import me.tatarka.inject.annotations.Component

@Component
@ApplicationScope
abstract class IosApplicationComponent: SharedApplicationComponent {

  companion object
}

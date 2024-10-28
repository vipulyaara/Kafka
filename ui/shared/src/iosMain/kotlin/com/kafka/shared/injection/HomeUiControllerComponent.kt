// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package com.kafka.shared.injection

import com.kafka.base.ActivityScope
import com.kafka.shared.KafkaUiViewController
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import platform.UIKit.UIViewController

@ActivityScope
@Component
abstract class HomeUiControllerComponent(
  @Component val applicationComponent: IosApplicationComponent,
) {

  abstract val uiViewControllerFactory: () -> UIViewController

  @Provides
  @ActivityScope
  fun uiViewController(bind: KafkaUiViewController): UIViewController = bind()

  companion object
}

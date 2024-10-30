package com.kafka.shared

import com.kafka.base.ActivityScope
import com.kafka.root.KafkaUiViewController
import com.kafka.root.SharedUiComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import platform.UIKit.UIViewController

@ActivityScope
@Component
abstract class HomeUiControllerComponent(
  @Component val applicationComponent: IosApplicationComponent,
): SharedUiComponent {

  abstract val uiViewControllerFactory: () -> UIViewController

  @Provides
  @ActivityScope
  fun uiViewController(bind: KafkaUiViewController): UIViewController = bind()

  companion object
}

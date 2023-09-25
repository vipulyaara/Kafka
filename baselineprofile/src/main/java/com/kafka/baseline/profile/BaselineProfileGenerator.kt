package com.kafka.baseline.profile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class generates a basic startup baseline profile for the target package.
 *
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the [baseline profile documentation](https://d.android.com/topic/performance/baselineprofiles)
 * for more information.
 *
 * You can run the generator with the Generate Baseline Profile run configuration,
 * or directly with `generateBaselineProfile` Gradle task:
 * ```
 * ./gradlew :app:generateReleaseBaselineProfile -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile
 * ```
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 *
 * Check [documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args)
 * for more information about available instrumentation arguments.
 *
 * After you run the generator, you can verify the improvements running the [StartupBenchmarks] benchmark.
 **/
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect("com.kafka.user") {
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll
            // through your most important UI.

            // Start default activity for your app
            pressHome()
            startActivityAndWait()

            waitForAsyncContent()
            scrollHomepageJourney()
//            goToItemDetailJourney()
        }
    }

    private fun MacrobenchmarkScope.waitForAsyncContent() {
        device.wait(Until.hasObject(By.res(homepageListKey)), 20_000)
//        val contentList = device.findObject(By.res(homepageListKey))
        // Wait until a snack collection item within the list is rendered.
//        contentList.wait(Until.hasObject(By.res("snack_collection")), 5_000)
    }

    fun MacrobenchmarkScope.scrollHomepageJourney() {
        val list = device.findObject(By.res(homepageListKey))
        // Set gesture margin to avoid triggering gesture navigation.
        list.setGestureMargin(device.displayWidth / 5)
        list.fling(Direction.DOWN)
        list.fling(Direction.DOWN)
        list.fling(Direction.UP)
        list.fling(Direction.UP)
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.goToItemDetailJourney() {
        val snackList = device.findObject(By.res(homepageListKey))
        val snacks = snackList.findObjects(By.res("content_item"))
        // Select snack from the list based on running iteration.
//        val index = (iteration ?: 0) % snacks.size
        snacks[0].click()
        // Wait until the screen is gone = the detail is shown.
        device.wait(Until.gone(By.res(homepageListKey)), 5_000)
        device.pressBack()
    }

    private val homepageListKey = "homepage_feed_items"
}

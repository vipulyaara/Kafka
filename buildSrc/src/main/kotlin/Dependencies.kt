import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.common() {
    implementation { Timber.core }
    implementation { Kotlin.stdlib }
}

fun DependencyHandlerScope.ui() {
    implementation { AndroidX.appCompat }
    implementation { AndroidX.fragment }
    implementation { AndroidX.material }
    implementation { AndroidX.workManager }

    implementation { Timber.core }
    implementation { Kotlin.stdlib }
}

fun DependencyHandlerScope.navigation() {
    implementation { AndroidX.Navigation.fragment }
    implementation { AndroidX.Navigation.ui }
}

fun DependencyHandlerScope.room() {
    implementation { AndroidX.Room.runtime }
    implementation { AndroidX.Room.ktx }
    kapt { AndroidX.Room.compiler }
}

fun DependencyHandlerScope.arch() {
    implementation { AndroidX.Arch.extensions }
    implementation { AndroidX.Arch.reactive_streams }
    kapt { AndroidX.Arch.compiler }
}

fun DependencyHandlerScope.ktx() {
    implementation { AndroidX.Ktx.core }
    implementation { AndroidX.Ktx.collection }
    implementation { AndroidX.Ktx.reactiveStreams }
    implementation { AndroidX.Ktx.sqlite }
    implementation { AndroidX.Ktx.viewmodel }
    implementation { AndroidX.Ktx.lifecycle }
}

fun DependencyHandlerScope.coroutines() {
    implementation { KotlinX.Coroutines.core }
    implementation { KotlinX.Coroutines.android }
}

fun DependencyHandlerScope.test() {
    testImplementation { AndroidX.Test.core }
    testImplementation { AndroidX.Test.junit }
    testImplementation { AndroidX.Test.rules }
    testImplementation { AndroidX.Arch.testing }
    testImplementation { AndroidX.Room.testing }
    testImplementation { Testing.Mockito.kotlin }
    testImplementation { Testing.PowerMock.core }
    testImplementation { Testing.PowerMock.api }
    testImplementation { Testing.PowerMock.module }
    testImplementation { RoboElectric.dependency }

    testImplementation { KotlinX.Coroutines.test }
    testImplementation { KotlinX.Coroutines.android }
    testImplementation { Mockk.core }
    kaptTest { AndroidX.Room.compiler }
}

fun DependencyHandlerScope.androidTest() {
    androidTestImplementation { AndroidX.annotation }
    androidTestImplementation { AndroidX.Test.junit }
    androidTestImplementation { AndroidX.Test.rules }
    androidTestImplementation { AndroidX.Espresso.core }
    androidTestImplementation { AndroidX.Espresso.intents }
    androidTestImplementation { AndroidX.Espresso.contrib }
}

fun DependencyHandlerScope.retrofit() {
    moshi()

    implementation { Retrofit.runtime }
    implementation { Retrofit.moshi }
    implementation { Retrofit.gson }
    implementation { OkHttp.core }
    implementation { OkHttp.loggingInterceptor }
}

fun DependencyHandlerScope.moshi() {
    implementation { Moshi.kotlin }
    kapt { Moshi.compiler }
}

private fun DependencyHandler.implementation(block: () -> String) {
    add("implementation", block())
}
private fun DependencyHandler.testImplementation(block: () -> String) {
    add("testImplementation", block())
}
private fun DependencyHandler.androidTestImplementation(block: () -> String) {
    add("androidTestImplementation", block())
}
private fun DependencyHandler.kapt(block: () -> String) {
    add("kapt", block())
}
private fun DependencyHandler.kaptTest(block: () -> String) {
    add("kaptTest", block())
}
private fun DependencyHandler.compileOnly(block: () -> String) {
    add("compileOnly", block())
}
private fun DependencyHandler.api(block: () -> String) {
    add("api", block())
}

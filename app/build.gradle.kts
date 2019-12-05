import Kotlin.kapt
import com.android.build.gradle.BaseExtension

dependencies {
    implementation(project(":data"))
    implementation(project(":player"))

    implementation(Kotlin.stdlib)
    implementation("org.jsoup:jsoup:1.12.1")
    implementation("androidx.asynclayoutinflater:asynclayoutinflater:1.0.0")

    implementation(AndroidX.appCompat)
    implementation(AndroidX.fragment)
    implementation(AndroidX.drawerLayout)
    implementation(AndroidX.material)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.workManager)
    implementation(AndroidX.viewPager2)
    implementation(AndroidX.palette)
    implementation(AndroidX.Paging.common)
    implementation(AndroidX.Paging.runtime)

    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)

    implementation(AndroidX.Room.runtime)
    kapt(AndroidX.Room.compiler)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(Retrofit.runtime)

    implementation(Dagger.dagger)
    implementation(Dagger.androidSupport)
    kapt(Dagger.compiler)
    kapt(Dagger.androidProcessor)

    compileOnly(AssistedInject.annotationDagger2)
    kapt(AssistedInject.processorDagger2)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.fragment)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)
    implementation(AndroidX.Ktx.lifecycle)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Serialization.dependency)

    implementation(Epoxy.core)
    implementation(Epoxy.dataBinding)
    kapt(Epoxy.processor)
    implementation(Epoxy.paging)
    implementation(Epoxy.preloading)

    implementation(MvRx.core)

    implementation(Lottie.core)

    implementation(Timber.core)

    implementation(Easeinterpolator.core)

    implementation(Coil.core)

//    implementation(ExpectAnim.core)

    implementation(Stetho.core)
    implementation(Stetho.urlConnection)

    implementation("com.github.jd-alexander:LikeButton:0.2.3")

//    implementation("org.threeten:threetenbp:1.3.7:no-tzdb")

//    implementation("me.everything:overscroll-decor-android:1.0.4")

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.0-beta-3")

    androidTestImplementation(AndroidX.annotation)
    androidTestImplementation(AndroidX.Test.junit)
    androidTestImplementation(AndroidX.Test.rules)
    androidTestImplementation(AndroidX.Espresso.core)
    androidTestImplementation(AndroidX.Espresso.intents)
    androidTestImplementation(AndroidX.Espresso.contrib)

    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    testImplementation(Testing.Mockito.kotlin)
    testImplementation(Testing.PowerMock.core)
    testImplementation(Testing.PowerMock.api)
    testImplementation(Testing.PowerMock.module)
}

configure<BaseExtension> {
    dataBinding {
        this.isEnabled = true
    }

    defaultConfig {
        applicationId = "org.kafka.user"
    }
}

apply(plugin = "com.google.gms.google-services")
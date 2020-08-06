import com.android.build.gradle.BaseExtension

dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.Language.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))
    implementation(project(Libs.Content.nameDependency))
    implementation(project(Libs.Player.nameDependency))

//    debugImplementation(LeakCanary.core)

    implementation(Store.core)
    implementation(Retrofit.runtime)

    mediaSession()

    common()
    arch()
    room()
    ktx()
    coroutines()

    ui()
    navigation()

    test()
    androidTest()

    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")
    implementation("com.google.android.play:core:1.8.0")

    implementation(AndroidX.appStartup)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Stetho.core)
    implementation(Stetho.urlConnection)
}

android {
    dynamicFeatures = mutableSetOf(":reader")
}

configure<BaseExtension> {
    defaultConfig {
        applicationId = Libs.applicationId
    }
    dataBinding.isEnabled = true
}

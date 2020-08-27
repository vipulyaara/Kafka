import com.android.build.gradle.BaseExtension

dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))
    implementation(project(Libs.Content.nameDependency))
    implementation(project(Libs.Player.nameDependency))
    implementation(project(Libs.Logger.nameDependency))

    implementation(Firebase.analytics)
    implementation(Firebase.dynamicLinks)

    implementation(Retrofit.runtime)

    mediaSession()

    implementation(ExoPlayer.player)

    common()
    arch()
    room()
    ktx()
    coroutines()

    ui()
    navigation()

    implementation("com.google.android.play:core:1.8.0")

    implementation(AndroidX.appStartup)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Stetho.core)
    implementation(Stetho.urlConnection)
}

configure<BaseExtension> {
    defaultConfig {
        applicationId = Libs.applicationId
    }
    dataBinding.isEnabled = true
}

apply(plugin = "com.google.gms.google-services")

dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))

    mediaSession()

    common()
    arch()
    ktx()
    coroutines()

    ui()
    navigation()

    implementation("com.google.android.exoplayer:extension-mediasession:2.9.4")
    implementation(ExoPlayer.player)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Coil.core)
}

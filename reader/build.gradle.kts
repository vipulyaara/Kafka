dependencies {
    implementation(project(Libs.App.nameDependency))
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))

    implementation(Store.core)
    implementation(FinestWebView.core)

    room()
    arch()
    ktx()
    coroutines()

    ui()
    navigation()

    test()
    androidTest()

    implementation("com.pdftron:pdftron:7.1.4")
    implementation("com.pdftron:tools:7.1.4")

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)
}

configure<com.android.build.gradle.BaseExtension> {
    defaultConfig {
        applicationId = "com.kafka.reader"
    }
}

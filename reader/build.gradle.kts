dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.UiCompose.nameDependency))
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


    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)
}

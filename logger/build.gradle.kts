dependencies {
    implementation(project(Libs.Data.nameDependency))

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)


    implementation(Firebase.analytics)
    implementation(Firebase.crashlytics)

    common()
    arch()
    room()
    ktx()
    coroutines()

    test()
    androidTest()

}

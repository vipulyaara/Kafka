dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.Domain.nameDependency))
    implementation(project(Libs.Language.nameDependency))
    implementation(project(Libs.UiCompose.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))

    implementation("info.androidhive:imagefilters:1.0.7")

    common()
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

dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))

    common()
    arch()
    ktx()
    coroutines()

    implementation(Store.core)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
}

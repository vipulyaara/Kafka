dependencies {
    api(project(Libs.BaseData.nameDependency))

    implementation(Jsoup.core)
    implementation(Store.core)
    implementation(ThreeTenBp.core)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    common()
    arch()
    room()
    coroutines()
    retrofit()
}

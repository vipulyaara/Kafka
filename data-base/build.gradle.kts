dependencies {
    implementation(Jsoup.core)
    implementation(Store.core)

    implementation(KotlinX.Serialization.dependency)
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
    test()
}

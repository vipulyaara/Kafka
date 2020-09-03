dependencies {
    implementation(project(Libs.BaseData.nameDependency))
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.Logger.nameDependency))

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(ThreeTenBp.core)
    implementation(Store.core)

    common()
    coroutines()

    test()
    androidTest()
}

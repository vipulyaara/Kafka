dependencies {
    implementation(project(":data"))
    implementation(ThreeTenBp.core)

    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")

    arch()
    room()
    ktx()
    coroutines()

    ui()
    navigation()

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Coil.core)
}

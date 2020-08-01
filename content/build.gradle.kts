dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.Language.nameDependency))
    implementation(project(Libs.Player.nameDependency))
    implementation(project(Libs.Reader.nameDependency))
    implementation(project(Libs.Logger.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))

    implementation("info.androidhive:imagefilters:1.0.7")

    ui()
    common()

    implementation(Store.core)

    arch()
    ktx()
    coroutines()
    retrofit()

    ui()
    navigation()

    test()
    androidTest()

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Coil.core)
}

configure<com.android.build.gradle.BaseExtension> {

    dataBinding.isEnabled = true
}


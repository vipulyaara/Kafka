import com.android.build.gradle.BaseExtension

dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.Domain.nameDependency))
    implementation(project(Libs.Language.nameDependency))
    implementation(project(Libs.UiCompose.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))
    implementation(project(Libs.Content.nameDependency))
    implementation(project(Libs.Player.nameDependency))
    implementation(project(Libs.Reader.nameDependency))

    implementation(Store.core)
    implementation(Retrofit.runtime)

    common()
    arch()
    room()
    ktx()
    coroutines()

    ui()
    navigation()

    test()
    androidTest()

    implementation(AndroidX.appStartup)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Stetho.core)
    implementation(Stetho.urlConnection)
}

configure<BaseExtension> {
    dataBinding {
        this.isEnabled = true
    }

    defaultConfig {
        applicationId = Libs.applicationId
    }
}

//apply(plugin = "com.google.gms.google-services")

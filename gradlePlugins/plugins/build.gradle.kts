plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
}

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradlePlug)
    implementation(libs.kotlin.gradlePlug)
    implementation(libs.google.gmsGoogleServices)
    implementation(libs.google.crashlyticsGradle)
    implementation(libs.hilt.gradlePlug)
}

gradlePlugin {
    plugins {
        create("plugins.android.library") {
            id = "plugins.android.library"
            implementationClass = "com.roadrunner.gradle.plugins.AndroidLibraryPlugin"
        }
    }
}

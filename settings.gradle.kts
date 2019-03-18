pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == Android.libPlugin) {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id == Jacoco.Android.plugin) {
                useModule("com.dicedmelon.gradle:jacoco-android:${requested.version}")
            }
            if (requested.id.id == KotlinX.Serialization.plugin) {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
            if (requested.id.id == PlayServices.plugin) {
                useModule("com.google.gms:google-services:4.0.1:${requested.version}")
            }
        }
    }
}

include(":app")
include(":data")
include(":player")

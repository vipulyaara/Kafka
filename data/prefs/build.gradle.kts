plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.base.domain)

                api(libs.dataStore)
                api(libs.kinject)
            }
        }
    }
}

android {
    namespace = "com.kafka.data.prefs"
}

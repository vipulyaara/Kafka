plugins {
    id("com.android.library") // todo: use build-logic
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

        val androidMain by getting {
            dependencies {
                api(libs.dataStore)
            }
        }
    }
}

android {
    namespace = "com.kafka.data.prefs"
}

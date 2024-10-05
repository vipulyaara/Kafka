plugins {
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

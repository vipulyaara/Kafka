plugins {
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlininject.runtime)
            }
        }
    }
}

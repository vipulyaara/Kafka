plugins {
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlininject.runtime)
            }
        }

        val jvmMain by getting
    }
}

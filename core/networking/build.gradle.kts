plugins {
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)

                implementation(libs.kotlininject.runtime)
                api(libs.ktor.client.cio)
                api(libs.ktor.client.logging)
                api(libs.kotlin.serialization)
            }
        }

        val jvmMain by getting
    }
}

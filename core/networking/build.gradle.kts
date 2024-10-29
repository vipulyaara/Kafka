plugins {
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)

                implementation(libs.kotlininject.runtime)
                api(libs.ktor.client.cio)
                api(libs.ktor.client.logging)
                api(libs.kotlin.serialization)
            }
        }
    }
}

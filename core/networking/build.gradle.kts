plugins {
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)

                implementation(libs.kotlin.serialization)

                api(libs.ktor.client.cio)
                api(libs.ktor.client.contentnegotiation)
                api(libs.ktor.client.logging)
                api(libs.ktor.serialization)
            }
        }
    }
}

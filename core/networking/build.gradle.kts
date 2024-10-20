plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.base.domain)

    implementation(libs.kotlininject.runtime)
    api(libs.ktor.client.core)
    api(libs.ktor.client.contentnegotiation)
    api(libs.ktor.client.java)
    api(libs.ktor.client.logging)
    api(libs.ktor.serialization)
}

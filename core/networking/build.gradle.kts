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
    api(libs.ktor.client.cio)
    api(libs.ktor.client.logging)
    api(libs.kotlin.serialization)
}

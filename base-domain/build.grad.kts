plugins {
    id("kotlin")
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.Timber.jdk)

    implementation(Libs.KotlinX.Coroutines.core)
    implementation(Libs.KotlinX.Coroutines.android)

    implementation(Libs.Dagger.dagger)
    implementation(Libs.Store.core)

    implementation(Libs.AndroidX.Paging.common)
}

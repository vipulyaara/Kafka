plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.kafka.recommendation"
}

dependencies {
    implementation(project(":base:domain"))
    implementation(project(":data:database"))
    implementation(project(":core:analytics"))

    implementation(libs.androidx.core)
    implementation(libs.firestore.ktx)
    implementation(libs.dataStore)
    implementation(libs.google.analytics)
    implementation(libs.google.messaging)
    implementation(libs.google.coroutines)
}

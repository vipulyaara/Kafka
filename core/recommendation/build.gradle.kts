plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.kafka.recommendation"
}

dependencies {
    implementation(projects.base.domain)
    implementation(projects.core.analytics)
    implementation(projects.data.database)
    implementation(projects.data.repo)

    implementation(libs.androidx.core)
    implementation(libs.firestore.ktx)
    implementation(libs.dataStore)
    implementation(libs.google.analytics)
    implementation(libs.google.messaging)
    implementation(libs.google.coroutines)
}

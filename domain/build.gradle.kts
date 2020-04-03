dependencies {
    implementation(project(Kafka.BaseData.nameDependency))
    implementation(project(Kafka.Data.nameDependency))

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)

    implementation(Dagger.dagger)
    kapt(Dagger.compiler)
    kapt(Dagger.androidProcessor)

    implementation(ThreeTenBp.core)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)
    implementation(KotlinX.Serialization.dependency)

    implementation(Timber.core)

    testImplementation(AndroidX.Test.core)
    testImplementation(AndroidX.Test.junit)
    testImplementation(AndroidX.Test.rules)
    testImplementation(KotlinX.Coroutines.test)
    testImplementation(KotlinX.Coroutines.android)

    testImplementation(Mockk.core)
    testImplementation(Testing.Mockito.kotlin)

    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    kaptTest(AndroidX.Room.compiler)

    kaptTest(Dagger.compiler)
}

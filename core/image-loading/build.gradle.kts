plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)

                api(libs.coil3.coil)
                api(libs.coil3.compose)
                api(libs.coil3.network)
                api(libs.coil3.svg)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommon)
            dependencies {
                api(libs.kotlin.coroutines.swing)
            }
        }

        androidMain {
            dependsOn(jvmCommon)
            dependencies {
            }
        }
    }
}

android {
    namespace = "com.kafka.image"
}

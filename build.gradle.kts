
import com.android.build.gradle.BaseExtension

buildscript {
    extra["kotlin_version"] = "1.3.70"
    extra["compose_version"] = "0.1.0-dev06"
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath(Android.gradlePlugin)
        classpath("com.google.gms:google-services:4.3.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${AndroidX.Navigation.pluginVersion}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://jitpack.io")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }

    val commonCompilerArgs = listOfNotNull(
        "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    )

    tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>> {
        kotlinOptions {
            allWarningsAsErrors = false
            freeCompilerArgs = commonCompilerArgs
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }
}

val libraryModules = listOf("data", "player", "ui", "reader")
val applicationModules = listOf("app")

subprojects {
    val isLibrary = project.name in libraryModules
    val isAndroid = project.name in libraryModules || project.name in applicationModules
    val isApplication = project.name in applicationModules

    if (isAndroid) {

        if (isLibrary) {
            apply {
                plugin(Android.libPlugin)
                plugin(Kotlin.androidPlugin)
                plugin(KotlinX.Serialization.plugin)
                plugin(Kotlin.androidExtensionsPlugin)
                plugin(Kotlin.kapt)
            }
        }

        if (isApplication) {
            apply {
                plugin(Android.appPlugin)
                plugin(Kotlin.androidPlugin)
                plugin(Kotlin.kapt)
                plugin(KotlinX.Serialization.plugin)
                plugin(Kotlin.androidExtensionsPlugin)
                plugin("androidx.navigation.safeargs.kotlin")
            }
        }

        configure<BaseExtension> {
            compileSdkVersion(Kafka.compileSdkVersion)


            defaultConfig {
                minSdkVersion(Kafka.minSdkVersion)
                targetSdkVersion(Kafka.compileSdkVersion)
                vectorDrawables.useSupportLibrary = true
                multiDexEnabled = true
                versionCode = 1
                versionName = Kafka.publishVersion
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            sourceSets {
                getByName("main").java.srcDirs("src/main/kotlin")
                getByName("test").java.srcDirs("src/test/kotlin")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    consumerProguardFiles("proguard-rules.pro")
                }
            }

            lintOptions {
                isAbortOnError = false
            }

//            testOptions {
//                unitTests.isReturnDefaultValues = true
//            }

            packagingOptions {
                exclude("META-INF/LICENSE.txt")
                exclude("META-INF/NOTICE.txt")
            }
        }
    }
}


import com.android.build.gradle.BaseExtension
import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.support.ReporterType

plugins {
    java
    kotlin("jvm") version Kotlin.version apply false
    id(Android.libPlugin) version Android.version apply false
    id(AndroidX.Navigation.plugin) version AndroidX.Navigation.pluginVersion apply false
    id(KotlinX.Serialization.plugin) version Kotlin.version apply false
    id(Ktlint.plugin) version Ktlint.version apply false
    id(PlayServices.plugin) version PlayServices.pluginVersion apply false
}

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.60-eap-76")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        jcenter()
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

    val androidModules = listOf("data", "player")
    val androidSampleModules = listOf("app")

    subprojects {
        val isAndroidModule = project.name in androidModules
        val isSample = project.name in androidSampleModules
        val isJvmModule = !isAndroidModule && !isSample

        if (isJvmModule) {
            apply {
                plugin(Kotlin.plugin)
            }

            dependencies {
                implementation(Kotlin.stdlib)
                testImplementation(JUnit.dependency)
            }

            configure<JavaPluginConvention> {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8

                sourceSets {
                    getByName("main").java.srcDirs("src/main/kotlin")
                    getByName("test").java.srcDirs("src/main/kotlin")
                }
            }
        }

        if (isAndroidModule) {
            apply {
                plugin(Android.libPlugin)
                plugin(Kotlin.androidPlugin)
                plugin(Kotlin.androidExtensionsPlugin)
            }


            configure<BaseExtension> {
                compileSdkVersion(Kafka.compileSdkVersion)

                defaultConfig {
                    minSdkVersion(Kafka.minSdkVersion)
                    targetSdkVersion(Kafka.compileSdkVersion)
                    versionCode = 1
                    versionName = Kafka.publishVersion
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

                testOptions {
                    unitTests.isReturnDefaultValues = true
                }
            }
        }

        if (!isSample) {
            apply {
                plugin(Release.MavenPublish.plugin)
                plugin(Release.Bintray.plugin)
                plugin(Ktlint.plugin)
            }

            configure<KotlinterExtension> {
                reporters = arrayOf(ReporterType.plain.name, ReporterType.checkstyle.name)
            }
        }
    }
}

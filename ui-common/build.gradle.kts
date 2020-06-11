fun org.gradle.api.publish.maven.MavenPom.addDependencies() = withXml {
    asNode().appendNode("dependencies").let { depNode ->
        configurations.implementation.allDependencies.forEach {
            depNode.appendNode("dependency").apply {
                appendNode("groupId", it.group)
                appendNode("artifactId", it.name)
                appendNode("version", it.version)
            }
        }
    }
}

publishing {
    publications {
        register(project.name, MavenPublication::class) {
            if (project.hasProperty("android")) {
                artifact("$buildDir/outputs/aar/${project.name}-release.aar") {
                    builtBy(tasks.getByPath("assemble"))
                }
            } else {
                from(components["java"])
            }
            groupId = "com.kafka"
            artifactId = project.name
            version = "0.0.2-alpha"

            pom {
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }
            }

            if (project.hasProperty("android")) {
                pom.addDependencies()
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/vipulyaara/kafka")
            credentials {
                username = "vipulyaara"
                password = "7684cd343d2db17c1f1830bd321a78980317993d"
            }
        }
    }
}


dependencies {
    implementation(project(":data"))

    implementation(Kotlin.stdlib)
    implementation(Jsoup.core)

    implementation(Store.core)

    implementation(AndroidX.appCompat)
    implementation(AndroidX.fragment)
    implementation(AndroidX.drawerLayout)
    implementation(AndroidX.material)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.workManager)
    implementation(AndroidX.viewPager2)
    implementation(AndroidX.palette)

    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)

    implementation(AndroidX.Room.runtime)
    kapt(AndroidX.Room.compiler)

    implementation(Retrofit.runtime)

    implementation(AndroidX.Arch.extensions)
    implementation(AndroidX.Arch.reactive_streams)
    kapt(AndroidX.Arch.compiler)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    compileOnly(AssistedInject.annotationDagger2)
    kapt(AssistedInject.processorDagger2)

    implementation(AndroidX.Ktx.core)
    implementation(AndroidX.Ktx.collection)
    implementation(AndroidX.Ktx.fragment)
    implementation(AndroidX.Ktx.palette)
    implementation(AndroidX.Ktx.reactiveStreams)
    implementation(AndroidX.Ktx.sqlite)
    implementation(AndroidX.Ktx.viewmodel)
    implementation(AndroidX.Ktx.lifecycle)

    implementation(KotlinX.Coroutines.core)
    implementation(KotlinX.Coroutines.android)

    implementation(Lottie.core)

    implementation(Timber.core)

    implementation(Easeinterpolator.core)

    implementation(Coil.core)

    implementation(Stetho.core)
    implementation(Stetho.urlConnection)

    androidTestImplementation(AndroidX.annotation)
    androidTestImplementation(AndroidX.Test.junit)
    androidTestImplementation(AndroidX.Test.rules)
    androidTestImplementation(AndroidX.Espresso.core)
    androidTestImplementation(AndroidX.Espresso.intents)
    androidTestImplementation(AndroidX.Espresso.contrib)

    testImplementation(AndroidX.Arch.testing)
    testImplementation(AndroidX.Room.testing)
    testImplementation(Testing.Mockito.kotlin)
    testImplementation(Testing.PowerMock.core)
    testImplementation(Testing.PowerMock.api)
    testImplementation(Testing.PowerMock.module)
}

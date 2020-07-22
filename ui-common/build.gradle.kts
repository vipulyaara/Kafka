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
    implementation(ThreeTenBp.core)


    arch()
    room()
    ktx()
    coroutines()

    ui()
    navigation()

    test()
    androidTest()

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)

    implementation(Coil.core)
}

rootProject.name = "logistics-rider-gradle-plugin"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":plugins")

enableFeaturePreview("VERSION_CATALOGS")

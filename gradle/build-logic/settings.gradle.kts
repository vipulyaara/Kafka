dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

buildCache {
    val remoteBuildCacheUrl =
        providers.gradleProperty("REMOTE_BUILD_CACHE_URL").orNull ?: return@buildCache
    val isCi = providers.environmentVariable("CI").isPresent

    local {
        isEnabled = !isCi
    }

    remote(HttpBuildCache::class) {
        url = uri(remoteBuildCacheUrl)
        isPush = isCi

        credentials {
            username = providers.gradleProperty("REMOTE_BUILD_CACHE_USERNAME").orNull
            password = providers.gradleProperty("REMOTE_BUILD_CACHE_PASSWORD").orNull
        }
    }
}

include(":convention")

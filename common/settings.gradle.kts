@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "common"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val pluginsDevPlatformVersion: String by settings
    versionCatalogs {
        create("pluginsDevPlatform") {
            from("io.github.zenhelix:gradle-plugins-dev-toml:$pluginsDevPlatformVersion")
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

include("gradle-test-common")
include("gradle-assertions")
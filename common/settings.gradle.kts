@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "common"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val pluginDevVersion: String by settings
    versionCatalogs {
        create("pluginsDevPlatform") {
            from("io.github.zenhelix:gradle-plugins-dev-toml:$pluginDevVersion")
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
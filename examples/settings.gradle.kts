@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "examples"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

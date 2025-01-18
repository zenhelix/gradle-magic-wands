@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "gradle-magic-wands"

includeBuild("common")
includeBuild("plugins")
includeBuild("examples")

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

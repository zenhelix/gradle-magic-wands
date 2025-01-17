gradlePlugin {
    plugins {
        create("kotlin-library.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinLibraryConventionPlugin"
            id = "io.github.zenhelix.kotlin-library.convention"
            displayName = "Gradle plugin for Kotlin library configuration"
            description = "A Gradle plugin that simplifies the setup and configuration of Kotlin libraries"
            tags = listOf("kotlin", "gradle-plugin", "build-tools", "compilation", "library")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("kotlin.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinConventionPlugin"
            id = "io.github.zenhelix.kotlin.convention"
            displayName = "Gradle plugin for streamlined Kotlin project setup"
            description = "A Gradle plugin that simplifies and enhances Kotlin project configuration"
            tags = listOf("kotlin", "gradle-plugin", "build-tools", "compilation")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}

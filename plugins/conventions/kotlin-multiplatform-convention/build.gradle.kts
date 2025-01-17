gradlePlugin {
    plugins {
        create("kmm.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformConventionPlugin"
            id = "io.github.zenhelix.kmm.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects, enabling seamless development for multiple platforms with shared code and platform-specific logic"
            tags = listOf("kotlin", "multiplatform", "kmm", "gradle-plugin", "build-tools", "configuration")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}

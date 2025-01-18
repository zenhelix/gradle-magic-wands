gradlePlugin {
    plugins {
        create("lombok.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.LombokConventionPlugin"
            id = "io.github.zenhelix.lombok.convention"
            displayName = "Gradle plugin for Lombok setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Lombok"
            tags = listOf("lombok", "java", "build-tools", "configuration", "code-generation")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
    compileOnly(pluginsDevPlatform.kotlin.lombok.gradle.plugin)
    compileOnly(pluginsDevPlatform.lombok.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("spring.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.SpringConventionPlugin"
            id = "io.github.zenhelix.spring.convention"
            displayName = "Gradle plugin for Spring project setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Spring-based projects"
            tags = listOf("spring", "gradle-plugin", "build-tools", "configuration", "java", "kotlin")
        }
    }
}


dependencies {
    compileOnly(pluginsDevPlatform.kotlin.jvm.gradle.plugin)

    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
    compileOnly(pluginsDevPlatform.kotlin.allopen.gradle.plugin)
    compileOnly(pluginsDevPlatform.kotlin.noarg.gradle.plugin)
}

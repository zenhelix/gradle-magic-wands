@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("maven-publish.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.PublishMavenConventionPlugin"
            id = "io.github.zenhelix.maven-publish.convention"
            displayName = "Gradle plugin streamlining maven-publish configuration"
            description = "A Gradle plugin that enhances and simplifies the configuration of the maven-publish plugin for seamless artifact publishing."
            tags = listOf("maven-publish", "publishing", "maven", "gradle-plugin", "build-tools", "configuration")
        }
    }
}

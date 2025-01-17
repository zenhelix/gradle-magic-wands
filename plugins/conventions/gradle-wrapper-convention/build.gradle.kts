@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("gradle-wrapper.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.GradleWrapperConventionPlugin"
            id = "io.github.zenhelix.gradle-wrapper.convention"
            displayName = "Gradle plugin streamlining gradle-wrapper usage"
            description = "A Gradle plugin that simplifies and enhances the use of gradle-wrapper by automating tasks and configurations"
            tags.set(listOf("gradle-wrapper", "wrapper", "build-tools", "configuration", "gradle-tools"))
        }
    }
}

dependencies {
    functionalTestImplementation(platform(pluginsDevPlatform.jackson.bom))
    functionalTestImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-properties")
    functionalTestImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks {
    jar {
        manifest {
            attributes("Gradle-Version" to project.gradle.gradleVersion)
        }
    }
}
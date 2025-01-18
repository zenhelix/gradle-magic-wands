gradlePlugin {
    plugins {
        create("kmm-compose-library") {
            implementationClass = "io.github.zenhelix.gradle.plugin.MultiplatformComposeLibraryPlugin"
            id = "io.github.zenhelix.kmm-compose-library"
            displayName = "Gradle plugin for KMM Compose Library setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform Compose projects"
            tags = listOf("kmm", "compose", "gradle-plugin", "build-tools", "configuration", "kotlin")
        }
    }
}

dependencies {
    implementation(pluginsDevPlatform.compose.gradle.plugin)
    implementation(pluginsDevPlatform.compose.compiler.gradle.plugin)

    implementation(rootProject.projects.multiplatformLibraryPlugin)
    implementation(rootProject.projects.kotlinMultiplatformComposeConvention)
}


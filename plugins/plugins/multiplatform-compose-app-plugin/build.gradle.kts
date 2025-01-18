gradlePlugin {
    plugins {
        create("kmm-compose-app") {
            implementationClass = "io.github.zenhelix.gradle.plugin.MultiplatformComposeAppPlugin"
            id = "io.github.zenhelix.kmm-compose-app"
            displayName = "Gradle plugin for KMM Compose Application setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform Compose projects"
            tags = listOf("kmm", "compose", "gradle-plugin", "build-tools", "configuration", "kotlin")
        }
    }
}

dependencies {
    implementation(pluginsDevPlatform.compose.gradle.plugin)
    implementation(pluginsDevPlatform.compose.compiler.gradle.plugin)

    implementation(rootProject.projects.multiplatformAppPlugin)
    implementation(rootProject.projects.kotlinMultiplatformComposeConvention)
}


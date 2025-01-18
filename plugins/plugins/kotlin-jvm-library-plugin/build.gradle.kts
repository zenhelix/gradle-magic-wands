gradlePlugin {
    plugins {
        create("kotlin-jvm-library") {
            implementationClass = "io.github.zenhelix.gradle.plugin.KotlinJvmLibraryPlugin"
            id = "io.github.zenhelix.kotlin-jvm-library"
            displayName = "Gradle plugin for Kotlin JVM library setup."
            description =
                "A Gradle plugin that simplifies the setup and configuration of Kotlin JVM libraries, including compilation, testing, and publishing, with built-in conventions and utilities."
            tags = listOf(
                "java", "kotlin", "library", "java-library", "kotlin-library", "jvm",
                "gradle-plugin", "build-tools", "compilation", "testing", "publishing", "configuration"
            )
        }
    }
}

dependencies {
    implementation(pluginsDevPlatform.kotlin.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.jvm.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.allopen.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.noarg.gradle.plugin)

    implementation(rootProject.projects.javaLibraryPlugin)
    implementation(rootProject.projects.kotlinJvmConvention)
    implementation(rootProject.projects.kotlinLibraryConvention)
}

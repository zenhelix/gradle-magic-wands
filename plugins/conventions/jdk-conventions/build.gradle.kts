@file:Suppress("UnstableApiUsage")

gradlePlugin {
    plugins {
        create("jdk8.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.Java8CompilationConventionPlugin"
            id = "io.github.zenhelix.jdk8.convention"
            displayName = "Gradle plugin for automated JDK setup"
            description = "A Gradle plugin that automates the setup and management of JDK installations for your build environmen"
            tags = listOf("jdk", "java", "gradle-plugin", "build-tools", "configuration")
        }
        create("jdk11.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.Java11CompilationConventionPlugin"
            id = "io.github.zenhelix.jdk11.convention"
            displayName = "Gradle plugin for automated JDK setup"
            description = "A Gradle plugin that automates the setup and management of JDK installations for your build environmen"
            tags = listOf("jdk", "java", "gradle-plugin", "build-tools", "configuration")
        }
        create("jdk17.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.Java17CompilationConventionPlugin"
            id = "io.github.zenhelix.jdk17.convention"
            displayName = "Gradle plugin for automated JDK setup"
            description = "A Gradle plugin that automates the setup and management of JDK installations for your build environmen"
            tags = listOf("jdk", "java", "gradle-plugin", "build-tools", "configuration")
        }
        create("jdk21.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.Java21CompilationConventionPlugin"
            id = "io.github.zenhelix.jdk21.convention"
            displayName = "Gradle plugin for automated JDK setup"
            description = "A Gradle plugin that automates the setup and management of JDK installations for your build environmen"
            tags = listOf("jdk", "java", "gradle-plugin", "build-tools", "configuration")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.jvm.gradle.plugin)
    compileOnly(pluginsDevPlatform.android.tools.gradle.plugin)
}
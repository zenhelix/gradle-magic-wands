gradlePlugin {
    plugins {
        create("proguard.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.ProguardConventionPlugin"
            id = "io.github.zenhelix.proguard.convention"
            displayName = "Gradle plugin for ProGuard configuration"
            description = "A Gradle plugin that simplifies and extends the configuration of ProGuard"
            tags = listOf("proguard", "android", "compose", "java", "gradle-plugin", "build-tools", "obfuscation", "optimization", "code-shrinking", "configuration")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.compose.gradle.plugin)
}

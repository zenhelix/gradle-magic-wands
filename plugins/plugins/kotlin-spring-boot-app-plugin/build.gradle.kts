gradlePlugin {
    plugins {
        create("kotlin.spring-boot-app") {
            implementationClass = "io.github.zenhelix.gradle.plugin.SpringBootApplicationKotlinPlugin"
            id = "io.github.zenhelix.kotlin.spring-boot-app"
            displayName = "Gradle plugin for Spring Boot setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Spring Boot projects"
            tags = listOf("spring", "spring-boot", "gradle-plugin", "build-tools", "configuration", "kotlin")
        }
    }
}

dependencies {
    implementation(pluginsDevPlatform.kotlin.jvm.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.allopen.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.noarg.gradle.plugin)

    implementation(rootProject.projects.springBootAppPlugin)
    implementation(rootProject.projects.kotlinJvmConvention)
}

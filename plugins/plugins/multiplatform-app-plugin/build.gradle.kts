gradlePlugin {
    plugins {
        create("kmm-app") {
            implementationClass = "io.github.zenhelix.gradle.plugin.MultiplatformAppPlugin"
            id = "io.github.zenhelix.kmm-app"
            displayName = "Gradle plugin for KMM Application setup"
            description =
                "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects"
            tags = listOf("kmm", "gradle-plugin", "build-tools", "configuration", "kotlin")
        }
    }
}

dependencies {
    implementation(pluginsDevPlatform.android.tools.gradle.plugin)
    implementation(pluginsDevPlatform.kotlin.gradle.plugin)

    implementation(rootProject.projects.publishMavenConvention)
    implementation(rootProject.projects.jdkConventions)
    implementation(rootProject.projects.javaConvention)

    implementation(rootProject.projects.androidConvention)
    implementation(rootProject.projects.androidSdkConventions)

    implementation(rootProject.projects.kotlinConvention)
    implementation(rootProject.projects.kotlinJvmConvention)
    implementation(rootProject.projects.kotlinMultiplatformConvention)
    implementation(rootProject.projects.kotlinMultiplatformJvmConvention)
    implementation(rootProject.projects.kotlinMultiplatformAndroidConvention)
    implementation(rootProject.projects.kotlinMultiplatformIosConvention)
    implementation(rootProject.projects.kotlinMultiplatformJsConvention)
    implementation(rootProject.projects.kotlinMultiplatformWasmJsConvention)
    implementation(rootProject.projects.kotlinMultiplatformWasmWasiConvention)

    api(rootProject.projects.distributionConvention)
    implementation(rootProject.projects.proguardConvention)
}

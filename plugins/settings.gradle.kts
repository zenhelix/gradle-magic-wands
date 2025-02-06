@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "plugins"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    val pluginDevVersion: String by settings
    versionCatalogs {
        create("pluginsDevPlatform") {
            from("io.github.zenhelix:gradle-plugins-dev-toml:$pluginDevVersion")
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id("io.github.zenhelix.maven-central-publish") version "0.5.0"
        id("com.gradle.plugin-publish") version "1.3.1"
    }
}

includeBuild("../common")

//The name with 'a' is special so that the project is the first to be processed. The artifactId itself is different
include("aa_catalog")

includeConvention("gradle-wrapper-convention")
includeConvention("publish-maven-convention")
includeConvention("jvm-test-suite-convention")
includeConvention("jacoco-convention")
includeConvention("jacoco-report-aggregation-convention")
includeConvention("jdk-conventions")
includeConvention("java-convention")
includeConvention("kotlin-convention")
includeConvention("kotlin-library-convention")
includeConvention("kotlin-jvm-convention")
includeConvention("kotlin-multiplatform-convention")
includeConvention("kotlin-multiplatform-jvm-convention")
includeConvention("kotlin-multiplatform-android-convention")
includeConvention("kotlin-multiplatform-ios-convention")
includeConvention("kotlin-multiplatform-js-convention")
includeConvention("kotlin-multiplatform-wasm-js-convention")
includeConvention("kotlin-multiplatform-wasm-wasi-convention")
includeConvention("kotlin-multiplatform-compose-convention")
includeConvention("android-sdk-conventions")
includeConvention("android-convention")
includeConvention("lombok-convention")
includeConvention("spring-convention")
includeConvention("spring-boot-convention")
includeConvention("distribution-convention")
includeConvention("proguard-convention")

includePlugin("java-library-plugin")
includePlugin("kotlin-jvm-library-plugin")
includePlugin("spring-boot-app-plugin")
includePlugin("kotlin-spring-boot-app-plugin")
includePlugin("spring-library-plugin")
includePlugin("spring-boot-autoconfigure-library-plugin")
includePlugin("spring-boot-starter-library-plugin")
includePlugin("multiplatform-app-plugin")
includePlugin("multiplatform-library-plugin")
includePlugin("multiplatform-compose-app-plugin")
includePlugin("multiplatform-compose-library-plugin")

fun includeConvention(projectPath: String, subdir: String = "conventions") {
    include(projectPath)
    project(":$projectPath").projectDir = file("$subdir/$projectPath")
}

fun includePlugin(projectPath: String, subdir: String = "plugins") {
    include(projectPath)
    project(":$projectPath").projectDir = file("$subdir/$projectPath")
}
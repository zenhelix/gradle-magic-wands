import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(pluginsDevPlatform.plugins.kotlin.jvm)
    `jacoco-report-aggregation`
    `jvm-test-suite`
}

allprojects {
    group = "io.github.zenhelix"
}

dependencies {
    allprojects.forEach {
        jacocoAggregation(project(":${it.path}"))
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "java-library")
    apply(plugin = "kotlin")


    val jdkVersion = JavaVersion.VERSION_17
    java {
        sourceCompatibility = jdkVersion
        targetCompatibility = jdkVersion

        withJavadocJar()
        withSourcesJar()
    }

    kotlin {
        explicitApi()
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(jdkVersion.toString()))
        }
    }
}

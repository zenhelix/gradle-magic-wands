@file:Suppress("UnstableApiUsage")

import org.gradle.api.publish.maven.plugins.MavenPublishPlugin.PUBLISH_LOCAL_LIFECYCLE_TASK_NAME
import org.gradle.api.publish.plugins.PublishingPlugin.PUBLISH_LIFECYCLE_TASK_NAME
import org.gradle.api.publish.plugins.PublishingPlugin.PUBLISH_TASK_GROUP
import org.gradle.language.base.plugins.LifecycleBasePlugin.ASSEMBLE_TASK_NAME
import org.gradle.language.base.plugins.LifecycleBasePlugin.BUILD_TASK_NAME
import org.gradle.language.base.plugins.LifecycleBasePlugin.CHECK_TASK_NAME
import org.gradle.language.base.plugins.LifecycleBasePlugin.CLEAN_TASK_NAME
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.Companion.fromTarget
import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    `kotlin-dsl`
    `jacoco-report-aggregation`
    `jvm-test-suite`
    id("com.gradle.plugin-publish") apply false
    signing
}

dependencies {
    allprojects.filter { it.name != "aa_catalog" }.forEach {
        jacocoAggregation(project(":${it.path}"))
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport"))
}

allprojects {
    group = "io.github.zenhelix"
}

configure(subprojects.filter { it.name != "aa_catalog" }) {
    apply(plugin = "org.gradle.kotlin.kotlin-dsl")
    apply(plugin = "jacoco")
    apply(plugin = "com.gradle.plugin-publish")
    apply(plugin = "signing")

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
            jvmTarget.set(fromTarget(jdkVersion.toString()))
        }
    }

    testing {
        suites {
            configureEach {
                if (this is JvmTestSuite) {
                    useJUnitJupiter()
                    dependencies {
                        implementation("io.github.zenhelix:gradle-assertions")
                    }
                }
            }

            val test by getting(JvmTestSuite::class) {
                testType.set(TestSuiteType.UNIT_TEST)
            }
            val functionalTest by registering(JvmTestSuite::class) {
                testType.set(TestSuiteType.FUNCTIONAL_TEST)

                dependencies {
                    implementation(project())
                    implementation(gradleTestKit())
                    implementation("io.github.zenhelix:gradle-test-common")
                }

                targets {
                    all { testTask.configure { shouldRunAfter(test) } }
                }
            }
        }
    }

    val functionalTest by sourceSets.existing
    configure<GradlePluginDevelopmentExtension> { testSourceSet(functionalTest.get()) }

    configurations {
        named<Configuration>("functionalTestImplementation").extendsFrom(configurations.testImplementation)
    }

    tasks.named("check") {
        dependsOn(testing.suites.named("functionalTest"))
    }

    configurations {
        testImplementation.get().extendsFrom(compileOnly.get())
    }

    reporting {
        reports {
            configureEach {
                if (this is JacocoCoverageReport) {
                    reportTask {
                        reports {
                            xml.required = true
                            html.required = true
                            csv.required = false
                        }
                    }
                }
            }
        }
    }

    tasks.jacocoTestReport {
        reports {
            xml.required = true
            html.required = true
            csv.required = false
        }
    }

    configure<PublishingExtension> {
        repositories {
            mavenLocal()
        }
    }

    signing {
        // ORG_GRADLE_PROJECT_signingKeyId
        val signingKeyId: String? by project
        // ascii-armored format
        // ORG_GRADLE_PROJECT_signingKey
        val signingKey: String? by project
        // ORG_GRADLE_PROJECT_signingPassword
        val signingPassword: String? by project

        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(extensions.getByType<PublishingExtension>().publications)
    }

    configure<GradlePluginDevelopmentExtension> {
        website = "https://github.com/zenhelix/gradle-magic-wands"
        vcsUrl = "https://github.com/zenhelix/gradle-magic-wands.git"
    }

}

listOf(
    PUBLISH_LIFECYCLE_TASK_NAME to PUBLISH_TASK_GROUP,
    PUBLISH_LOCAL_LIFECYCLE_TASK_NAME to PUBLISH_TASK_GROUP,
    "publishPlugins" to PUBLISH_TASK_GROUP,
).forEach { (taskName, taskGroup) ->
    tasks.register(taskName) {
        group = taskGroup
    }
}

listOf(
    CLEAN_TASK_NAME, ASSEMBLE_TASK_NAME, BUILD_TASK_NAME,
    CHECK_TASK_NAME, "jacocoTestReport", "testCodeCoverageReport",
    PUBLISH_LIFECYCLE_TASK_NAME, PUBLISH_LOCAL_LIFECYCLE_TASK_NAME, "publishPlugins"
).forEach { taskName ->
    tasks.findByName(taskName)?.also { task ->
        task.dependsOn(project.subprojects.mapNotNull { it.tasks.findByName(taskName) })
    }
}
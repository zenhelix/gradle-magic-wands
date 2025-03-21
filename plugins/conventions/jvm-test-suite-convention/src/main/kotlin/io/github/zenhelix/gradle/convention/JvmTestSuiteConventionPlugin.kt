package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.register
import org.gradle.testing.base.TestingExtension

public class JvmTestSuiteConventionPlugin : Plugin<Project> {

    public companion object {
        public const val JVM_TEST_SUITE_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jvm-test-suite.convention"
    }

    @Suppress("UnstableApiUsage")
    override fun apply(target: Project) {
        //without afterEvaluate, so that configurations for testSuite are automatically created
        with(target) {
            extensions.findByType<TestingExtension>()?.also {
                extensions.configure<TestingExtension> {
                    suites.configureEach {
                        if (this is JvmTestSuite) {
                            useJUnitJupiter()
                        }
                    }

                    val test = suites.getByName<JvmTestSuite>("test")

                    val functionalTest = suites.register<JvmTestSuite>("functionalTest") {
                        configurations.named(sources.implementationConfigurationName) {
                            extendsFrom(configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME))
                        }
                        targets.all {
                            testTask.configure { shouldRunAfter(test) }
                        }
                        dependencies {
                            implementation.add(project())
                        }
                    }
                    suites.register<JvmTestSuite>("integrationTest") {
                        configurations.named(sources.implementationConfigurationName) {
                            extendsFrom(configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME))
                        }
                        targets.all {
                            testTask.configure { shouldRunAfter(functionalTest) }
                        }
                        dependencies {
                            implementation.add(project())
                        }
                    }
                    suites.register<JvmTestSuite>("performanceTest") {
                        configurations.named(sources.implementationConfigurationName) {
                            extendsFrom(configurations.getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME))
                        }
                        targets.all {
                            testTask.configure { shouldRunAfter(test) }
                        }
                        dependencies {
                            implementation.add(project())
                        }
                    }
                }
            }
        }
    }
}
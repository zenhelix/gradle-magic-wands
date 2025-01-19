package io.github.zenhelix.gradle.convention

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.zenhelix.gradle.convention.ExtraGradleWrapperExtension.Companion.GRADLE_WRAPPER_DIST_URL
import io.github.zenhelix.gradle.convention.GradleWrapperConventionPlugin.Companion.DEFAULT_FALLBACK_VERSION
import io.github.zenhelix.gradle.convention.GradleWrapperConventionPlugin.Companion.GRADLE_WRAPPER_CONVENTION_PLUGIN_ID
import io.github.zenhelix.gradle.test.gradleRunner
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class GradleWrapperConventionPluginFunctionalTest {

    private companion object {
        @TempDir
        private lateinit var testProjectDir: File

        private lateinit var settingsFile: File
        private lateinit var rootBuildFile: File

        private lateinit var fakeWrapperFile: File
        private lateinit var wrapperPropertiesFile: File

        private val propsMapper = JavaPropsMapper().registerModule(kotlinModule())
        private const val CURRENT_GRADLE_VERSION = "8.12"
        private const val GRADLE_BASE_URL_PROPERTY_NAME: String = "org.gradle.internal.services.base.url"
    }

    @BeforeEach
    fun setup() {
        System.clearProperty(GRADLE_BASE_URL_PROPERTY_NAME)
        settingsFile = File(testProjectDir, "settings.gradle.kts")
        rootBuildFile = File(testProjectDir, "build.gradle.kts")
        fakeWrapperFile = File(testProjectDir, "gradle-$DEFAULT_FALLBACK_VERSION-bin.zip").apply { createNewFile() }
        wrapperPropertiesFile = testProjectDir.resolve("gradle").resolve("wrapper").resolve("gradle-wrapper.properties")
    }

    @AfterEach
    fun afterEach() {
        System.clearProperty(GRADLE_BASE_URL_PROPERTY_NAME)
    }

    @Test
    fun defaultWrapper() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") }
            tasks.wrapper {
                validateDistributionUrl.set(false)
            }
            """
        )

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                validateDistributionUrl = false,
                distributionUrl = "https://services.gradle.org/distributions/gradle-$DEFAULT_FALLBACK_VERSION-bin.zip",
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    @Test
    fun overrideBaseUrlWrapper() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") }
            gradleWrapperExt {
                baseUrl = "${testProjectDir.toURI().toURL()}"
            }
            """
        )

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                distributionUrl = fakeWrapperFile.toURI().toURL().toString(),
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    @Test
    fun overrideTaskWrapper() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") }
            tasks.wrapper {
                distributionUrl = "${fakeWrapperFile.toURI().toURL()}"
            }
            """
        )

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                distributionUrl = fakeWrapperFile.toURI().toURL().toString(),
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    @Test
    fun overrideVersionWrapper() {
        val version = "1.0"
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") }
            tasks.wrapper {
                validateDistributionUrl.set(false)
                gradleVersion = "$version"
            }
            """
        )

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                validateDistributionUrl = false,
                distributionUrl = "${GRADLE_WRAPPER_DIST_URL}gradle-$version-bin.zip"
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    @Test
    fun disabledPluginWithParameter() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") }
            tasks.wrapper {
                validateDistributionUrl.set(false)
            }
            gradleWrapperExt {
                enabled = false
            }
            """
        )

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                validateDistributionUrl = false,
                distributionUrl = "https://services.gradle.org/distributions/gradle-$CURRENT_GRADLE_VERSION-bin.zip",
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    @Test
    fun disabledPlugin() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") apply false }
            tasks.wrapper {
                validateDistributionUrl.set(false)
            }
            """
        )

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                validateDistributionUrl = false,
                distributionUrl = "https://services.gradle.org/distributions/gradle-$CURRENT_GRADLE_VERSION-bin.zip",
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    @Test
    fun overrideBaseUrlWithEnvironmentVariable() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$GRADLE_WRAPPER_CONVENTION_PLUGIN_ID") }
            tasks.wrapper {
                validateDistributionUrl.set(false)
            }
            """
        )

        val otherBaseUrl = "https://blabla.org"
        System.setProperty(GRADLE_BASE_URL_PROPERTY_NAME, otherBaseUrl)

        gradleRunner(testProjectDir) { withArguments("wrapper") }

        assertEquals(
            GradleWrapperProperties(
                validateDistributionUrl = false,
                distributionUrl = "$otherBaseUrl/distributions/gradle-$DEFAULT_FALLBACK_VERSION-bin.zip",
            ),
            propsMapper.readValue<GradleWrapperProperties>(wrapperPropertiesFile)
        )
    }

    private data class GradleWrapperProperties(
        val distributionBase: String? = "GRADLE_USER_HOME",
        val distributionPath: String? = "wrapper/dists",
        val distributionUrl: String?,
        val networkTimeout: Long? = 10_000,
        val validateDistributionUrl: Boolean? = true,
        val zipStoreBase: String? = "GRADLE_USER_HOME",
        val zipStorePath: String? = "wrapper/dists"
    )
}
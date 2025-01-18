package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.SpringBootConventionPlugin.Companion.SPRING_BOOT_CONVENTION_PLUGIN_ID
import io.github.zenhelix.gradle.test.gradleRunner
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.internal.project.ProjectInternal.TASKS_TASK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOOT_JAR_TASK_NAME
import java.io.File

class SpringBootConventionPluginFunctionalTest {

    private companion object {
        @TempDir
        private lateinit var testProjectDir: File

        private lateinit var settingsFile: File
        private lateinit var rootBuildFile: File
    }

    @BeforeEach
    fun setup() {
        settingsFile = File(testProjectDir, "settings.gradle.kts")
        rootBuildFile = File(testProjectDir, "build.gradle.kts")
    }

    @Test
    fun `without spring plugin`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$SPRING_BOOT_CONVENTION_PLUGIN_ID") }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).doesNotContain(BOOT_JAR_TASK_NAME)
    }

    @Test
    fun `default behavior`() {
        settingsFile.writeText(""" rootProject.name = "test" """)
        rootBuildFile.writeText(
            """
            plugins {
                id("$SPRING_BOOT_CONVENTION_PLUGIN_ID")
                id("org.springframework.boot")
                java
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(BOOT_JAR_TASK_NAME, "bootBuildInfo")
    }

    @Test
    fun `disabled plugin`() {
        settingsFile.writeText(""" rootProject.name = "test" """)
        rootBuildFile.writeText(
            """
            plugins { 
                id("$SPRING_BOOT_CONVENTION_PLUGIN_ID") apply false
                id("org.springframework.boot")
                java
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(BOOT_JAR_TASK_NAME).doesNotContain("bootBuildInfo")
    }

}
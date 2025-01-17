package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.JavaConventionPlugin.Companion.JAVA_CONVENTION_PLUGIN_ID
import io.github.zenhelix.gradle.test.gradleRunner
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.attributes.DocsType.JAVADOC
import org.gradle.api.attributes.DocsType.SOURCES
import org.gradle.api.internal.project.ProjectInternal.TASKS_TASK
import org.gradle.api.internal.tasks.JvmConstants.JAR_TASK_NAME
import org.gradle.api.internal.tasks.JvmConstants.JAVADOC_TASK_NAME
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class JavaConventionPluginFunctionalTest {

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
    fun `without java plugin`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { id("$JAVA_CONVENTION_PLUGIN_ID") }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).doesNotContain(JAR_TASK_NAME, JAVADOC_TASK_NAME, "${JAVADOC}Jar", "${SOURCES}Jar")
    }

    @Test
    fun `default behavior`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID")
                java
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME, "${JAVADOC}Jar", "${SOURCES}Jar")
    }

    @Test
    fun `plugin not applied`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID") apply false
                java
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME).doesNotContain("${JAVADOC}Jar", "${SOURCES}Jar")
    }

    @Test
    fun `plugin disabled`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID")
                java
            }
            
            javaExt {
                enabled = false
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME).doesNotContain("${JAVADOC}Jar", "${SOURCES}Jar")
    }

    @Test
    fun `javadocJar disabled`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID")
                java
            }
            
            javaExt {
                withJavadocJar = false
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME, "${SOURCES}Jar").doesNotContain("${JAVADOC}Jar")
    }

    @Test
    fun `javadocJar disabled in plugin but otherwise`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID")
                java
            }
            
            javaExt {
                withJavadocJar = false
            }
            
            java {
                withJavadocJar()
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME, "${SOURCES}Jar", "${JAVADOC}Jar")
    }

    @Test
    fun `sourcesJar disabled`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID")
                java
            }
            
            javaExt {
                withSourcesJar = false
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME, "${JAVADOC}Jar").doesNotContain("${SOURCES}Jar")
    }

    @Test
    fun `sourcesJar disabled in plugin but otherwise`() {
        settingsFile.writeText("rootProject.name = \"test\"")
        rootBuildFile.writeText(
            """
            plugins { 
                id("$JAVA_CONVENTION_PLUGIN_ID")
                java
            }
            
            javaExt {
                withSourcesJar = false
            }
            
            java {
                withSourcesJar()
            }
            """
        )

        val output = gradleRunner(testProjectDir) { withArguments(TASKS_TASK, "--all") }.output
        assertThat(output).contains(JAR_TASK_NAME, JAVADOC_TASK_NAME, "${SOURCES}Jar", "${JAVADOC}Jar")
    }

}
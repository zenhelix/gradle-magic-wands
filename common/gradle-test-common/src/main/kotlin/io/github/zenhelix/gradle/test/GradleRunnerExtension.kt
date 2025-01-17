package io.github.zenhelix.gradle.test

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import java.io.File

public fun gradleRunner(projectDir: File, classpath: Iterable<File> = emptyList<File>(), initializer: GradleRunner.() -> Unit = {}): BuildResult {
    val builder = GradleRunner.create()
        .withProjectDir(projectDir)
        .withPluginClasspath(PluginUnderTestMetadataReading.readImplementationClasspath() + classpath)
    builder.initializer()
    return builder.build()
}

public fun gradleRunnerDebug(
    projectDir: File,
    classpath: Iterable<File> = emptyList<File>(),
    initializer: GradleRunner.() -> Unit = {}
): BuildResult = gradleRunner(projectDir, classpath) {
    withDebug(true).forwardOutput().initializer()
}

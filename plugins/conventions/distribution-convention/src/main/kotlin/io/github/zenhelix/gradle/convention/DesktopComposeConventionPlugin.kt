package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.DistributionConventionPlugin.Companion.KOTLIN_COMPOSE_PLUGIN_NAME
import io.github.zenhelix.gradle.convention.DistributionExtension.Companion.DEFAULT_FINAL_OUTPUT_DESTINATION
import io.github.zenhelix.gradle.convention.DistributionExtension.Companion.DISTRIBUTION_TASK_GROUP_NAME
import io.github.zenhelix.gradle.convention.DistributionExtension.Companion.EXTRA_DISTRIBUTION_EXTENSION_NAME
import io.github.zenhelix.gradle.convention.utils.DistributionName
import io.github.zenhelix.gradle.convention.utils.currentOS
import io.github.zenhelix.gradle.convention.utils.lowerCamelCaseName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractJPackageTask

public class DesktopComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val distributionExtension =
            target.extensions.findByType<DistributionExtension>() ?: target.extensions.create<DistributionExtension>(EXTRA_DISTRIBUTION_EXTENSION_NAME)

        target.afterEvaluate {
            val composeDesktopPluginInClasspath = runCatching { Class.forName("org.jetbrains.compose.desktop.application.dsl.TargetFormat") }.isSuccess
            if (composeDesktopPluginInClasspath.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin(KOTLIN_COMPOSE_PLUGIN_NAME) {

                tasks.withType<AbstractJPackageTask>().filter { it.targetFormat != TargetFormat.AppImage }.forEach { packageTask ->
                    val fromFile = packageTask.destinationDir.file(packageTask.packageName.map {
                        it + packageTask.versionPostfix().get() + packageTask.targetFormat.fileExt
                    })

                    tasks.register(lowerCamelCaseName("rename", packageTask.name)) {
                        dependsOn(packageTask)

                        group = DISTRIBUTION_TASK_GROUP_NAME
                        description = "Rename the binary file"

                        doLast {
                            if (fromFile.isPresent && fromFile.get().asFile.exists()) {
                                val currentFile = fromFile.get().asFile

                                val newName = DistributionName.desktop(packageTask)

                                currentFile.renameTo(currentFile.parentFile.resolve(newName))
                            }
                        }
                    }

                    val copyAndRenameTask = tasks.register<Copy>(lowerCamelCaseName("copyRename", packageTask.name)) {
                        dependsOn(packageTask)

                        group = DISTRIBUTION_TASK_GROUP_NAME
                        description = "Copy and rename the binary file"

                        from(fromFile)
                        into(
                            layout.buildDirectory
                                .dir(distributionExtension.finalOutputDestination.convention(DEFAULT_FINAL_OUTPUT_DESTINATION))
                                .map { it.dir(currentOS.id) }
                        )
                        rename { DistributionName.desktop(packageTask) }
                    }

                    packageTask.finalizedBy(copyAndRenameTask)
                }


                tasks.withType<Jar>().named { it.endsWith("uberJarForCurrentOS", true) }.forEach { jarTask ->
                    val copyUberJarTask = tasks.register<Copy>(lowerCamelCaseName("copy", jarTask.name)) {
                        dependsOn(jarTask)

                        group = DISTRIBUTION_TASK_GROUP_NAME
                        description = "Copy uberJar"

                        from(jarTask)
                        into(
                            layout.buildDirectory
                                .dir(distributionExtension.finalOutputDestination.convention(DEFAULT_FINAL_OUTPUT_DESTINATION))
                                .map { it.dir(currentOS.id) }
                        )
                    }

                    jarTask.finalizedBy(copyUberJarTask)
                }
            }
        }
    }

    @Suppress("UselessCallOnNotNull")
    private fun AbstractJPackageTask.versionPostfix() = this.packageVersion.map {
        if (!it.isNullOrBlank()) {
            "-$it"
        } else {
            ""
        }
    }
}
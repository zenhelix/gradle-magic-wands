package io.github.zenhelix.gradle.convention.utils

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractJPackageTask
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

internal class DistributionName private constructor() {

    internal companion object {
        fun desktop(packageTask: AbstractJPackageTask): String = desktop(
            packageName = packageTask.packageName.get(),
            version = packageTask.packageVersion.orNull,
            flavour = packageTask.toDistributionName(),
            format = packageTask.targetFormat
        )

        fun desktop(packageName: String, version: String?, flavour: DistributionFlavour, format: TargetFormat): String {
            return "${packageName}-${currentArch.id}${version?.takeIf { it.isNotBlank() }?.let { "-$it" } ?: ""}-${flavour.id()}${format.fileExt}"
        }

        fun web(
            moduleName: String,
            version: String?,
            flavour: DistributionFlavour,
            target: KotlinJsIrTarget
        ): String {
            return "${moduleName}-${target.name}${version?.takeIf { it.isNotBlank() }?.let { "-$it" } ?: ""}-${flavour.id()}.zip"
        }
    }


}

private fun AbstractJPackageTask.toDistributionName() = if (this.name.contains("release", true)) {
    DistributionFlavour.RELEASE
} else {
    DistributionFlavour.DEBUG
}

internal enum class DistributionFlavour(private val id: String) {
    RELEASE("release"),
    DEBUG("debug");

    internal fun id() = id

}
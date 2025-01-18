package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class DistributionExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_DISTRIBUTION_PLUGIN)

    public val outputDestination: Property<String> = objects.property<String>().convention(DEFAULT_OUTPUT_DESTINATION)
    public val finalOutputDestination: Property<String> = objects.property<String>().convention(DEFAULT_FINAL_OUTPUT_DESTINATION)

    public companion object {
        public const val EXTRA_DISTRIBUTION_EXTENSION_NAME: String = "distributionExt"

        public const val DEFAULT_ENABLED_DISTRIBUTION_PLUGIN: Boolean = true

        public const val DISTRIBUTION_TASK_GROUP_NAME: String = "distribution"

        public const val DEFAULT_OUTPUT_DESTINATION: String = "distributions"
        public const val DEFAULT_FINAL_OUTPUT_DESTINATION: String = "out"
    }

}
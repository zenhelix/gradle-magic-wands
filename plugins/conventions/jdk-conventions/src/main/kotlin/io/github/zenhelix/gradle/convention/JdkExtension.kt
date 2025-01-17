package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class JdkExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_JDK_PLUGIN)

    public companion object {
        public const val JDK_EXTENSION_NAME: String = "jdk"

        public const val DEFAULT_ENABLED_JDK_PLUGIN: Boolean = true
    }
}
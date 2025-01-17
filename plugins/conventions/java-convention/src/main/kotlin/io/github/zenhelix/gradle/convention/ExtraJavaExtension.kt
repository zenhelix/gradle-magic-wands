package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraJavaExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_JAVA_PLUGIN)

    public val withJavadocJar: Property<Boolean> = objects.property<Boolean>()
    public val withSourcesJar: Property<Boolean> = objects.property<Boolean>()

    public companion object {
        public const val EXTRA_JAVA_EXTENSION_NAME: String = "javaExt"

        public const val DEFAULT_ENABLED_EXTRA_JAVA_PLUGIN: Boolean = true
    }
}
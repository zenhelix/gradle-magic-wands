package io.github.zenhelix.gradle.convention

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

public open class ExtraMavenPublishExtension @Inject constructor(objects: ObjectFactory) {

    public val enabled: Property<Boolean> = objects.property<Boolean>().convention(DEFAULT_ENABLED_EXTRA_MAVEN_PUBLISH_PLUGIN)

    public companion object {
        public const val EXTRA_MAVEN_PUBLISH_EXTENSION_NAME: String = "mavenPublishExt"

        public const val DEFAULT_ENABLED_EXTRA_MAVEN_PUBLISH_PLUGIN: Boolean = true
    }
}
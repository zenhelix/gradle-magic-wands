package io.github.zenhelix.gradle.assertion

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencySet

public class ProjectAssert(actual: Project) : AbstractAssert<ProjectAssert, Project>(actual, ProjectAssert::class.java) {

    public companion object {
        public fun assertThat(actual: Project): ProjectAssert = ProjectAssert(actual)
    }

    public fun hasDependency(configuration: String, group: String?, module: String, version: String?): ProjectAssert {
        Assertions.assertThat(actual.configurations.findByName(configuration)?.dependencies?.exist(group, module, version)).isNotNull().isTrue()
        return this
    }

    public fun hasInImplementation(group: String?, module: String, version: String? = null): ProjectAssert {
        hasDependency("implementation", group = group, module = module, version = version)
        return this
    }

    public fun hasInRuntimeOnly(group: String?, module: String, version: String? = null): ProjectAssert {
        hasDependency("runtimeOnly", group = group, module = module, version = version)
        return this
    }

    public fun hasInTestImplementation(group: String?, module: String, version: String? = null): ProjectAssert {
        hasDependency("testImplementation", group = group, module = module, version = version)
        return this
    }

    public fun hasInAnnotationProcessor(group: String?, module: String, version: String? = null): ProjectAssert {
        hasDependency("annotationProcessor", group = group, module = module, version = version)
        return this
    }

    public fun isEmptyConfiguration(configuration: String): ProjectAssert {
        Assertions.assertThat(actual.configurations.findByName(configuration)?.dependencies?.isEmpty()).describedAs("$configuration is not empty").isNotNull()
            .isTrue()
        return this
    }

    public fun isEmptyImplementation(): ProjectAssert {
        isEmptyConfiguration("implementation")
        return this
    }

    public fun isEmptyRuntimeOnly(): ProjectAssert {
        isEmptyConfiguration("runtimeOnly")
        return this
    }

    public fun isEmptyTestImplementation(): ProjectAssert {
        isEmptyConfiguration("testImplementation")
        return this
    }

    public fun isEmptyAnnotationProcessor(): ProjectAssert {
        isEmptyConfiguration("annotationProcessor")
        return this
    }

    public fun isAllConfigurationsEmpty(): ProjectAssert {
        Assertions.assertThat(actual.configurations.flatMap { it.allDependencies }).isEmpty()
        return this
    }

    public fun containsOnlyInConfiguration(configuration: String, vararg notions: String): ProjectAssert {
        Assertions.assertThat(actual.configurations.getByName(configuration).dependencies.map {
            listOfNotNull(it.group, it.name, it.version).joinToString(":")
        }).containsOnly(*notions)
        return this
    }

    public fun containsOnlyInImplementation(vararg notions: String): ProjectAssert {
        containsOnlyInConfiguration("implementation", *notions)
        return this
    }

    public fun containsOnlyInRuntimeOnly(vararg notions: String): ProjectAssert {
        containsOnlyInConfiguration("runtimeOnly", *notions)
        return this
    }

    public fun containsOnlyInTestRuntimeOnly(vararg notions: String): ProjectAssert {
        containsOnlyInConfiguration("testRuntimeOnly", *notions)
        return this
    }

    public fun containsOnlyInTestImplementation(vararg notions: String): ProjectAssert {
        containsOnlyInConfiguration("testImplementation", *notions)
        return this
    }

    public fun containsOnlyInAnnotationProcessor(vararg notions: String): ProjectAssert {
        containsOnlyInConfiguration("annotationProcessor", *notions)
        return this
    }

    private fun DependencySet.find(group: String?, module: String, version: String? = null) =
        this.find { it.group == group && it.name == module && it.version == version }

    private fun DependencySet.exist(group: String?, module: String, version: String? = null) = this.find(group, module, version) != null

}
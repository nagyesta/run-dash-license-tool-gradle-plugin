/*
 * SPDX-License-Identifier: MIT
 */

import org.cyclonedx.Version
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version libs.versions.kotlin.get()
    `maven-publish`
    alias(libs.plugins.plugin.publish)
    alias(libs.plugins.versioner)
    alias(libs.plugins.index.scan)
    alias(libs.plugins.owasp.dependencycheck)
    alias(libs.plugins.cyclonedx.bom)
    alias(libs.plugins.licensee.plugin)
}

group = "io.github.nagyesta"

versioner {
    startFrom {
        major = 0
        minor = 0
        patch = 0
    }
    match {
        major = "{major}"
        minor = "{minor}"
        patch = "{patch}"
    }
    pattern {
        pattern = "%M.%m.%p"
    }
    git {
        authentication {
            https {
                token = project.properties["githubToken"]?.toString()
            }
        }
    }
    tag {
        prefix = "v"
        useCommitMessage = true
    }
}

tasks {
    withType<KotlinCompile> { compilerOptions { jvmTarget = JvmTarget.JVM_17 } }
}

dependencies {
    implementation(libs.kotlin.stdlib)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation(libs.jupiter.core)
    testImplementation(gradleTestKit())
}

gradlePlugin {
    website.set("https://github.com/nagyesta/run-dash-license-tool-gradle-plugin")
    vcsUrl.set("https://github.com/nagyesta/run-dash-license-tool-gradle-plugin")
    plugins {
        create("runDashLicenseToolPlugin", fun PluginDeclaration.() {
            displayName = "Run Dash License Tool Gradle Plugin"
            description = "Adds a convenient task to run the Eclipse Dash License Tool on your Gradle build"
            id = "io.github.nagyesta.run-dash-license-tool-gradle-plugin"
            implementationClass = "com.github.nagyesta.rundash.gradle.RunDashLicenseToolPlugin"
            tags.set(listOf("dash-license-tool", "license-check", "license", "dash", "run-dash"))
        })
    }
}


tasks.cyclonedxBom {
    projectType.set(org.cyclonedx.model.Component.Type.LIBRARY)
    schemaVersion.set(Version.VERSION_16)
    includeConfigs.set(listOf("runtimeClasspath"))
    skipConfigs.set(listOf("compileClasspath", "testCompileClasspath"))
    skipProjects.set(listOf())
    jsonOutput = project.layout.buildDirectory.file("reports/bom.json").get().asFile
    //noinspection UnnecessaryQualifiedReference
    val attachmentText = org.cyclonedx.model.AttachmentText()
    attachmentText.text = Base64.getEncoder().encodeToString(
        file("${project.rootProject.projectDir}/LICENSE").readBytes()
    )
    attachmentText.encoding = "base64"
    attachmentText.contentType = "text/plain"
    //noinspection UnnecessaryQualifiedReference
    val license = org.cyclonedx.model.License()
    license.name = "MIT License"
    license.setLicenseText(attachmentText)
    license.url = "https://raw.githubusercontent.com/nagyesta/run-dash-license-tool-gradle-plugin/main/LICENSE"
    licenseChoice = org.cyclonedx.model.LicenseChoice().apply {
        addLicense(license)
    }
}

licensee {
    allow("Apache-2.0")
}

val copyLegalDocs = tasks.register("copyLegalDocs", Copy::class) {
    group = "documentation"
    description = "Copies legal files and reports."
    from(file("${project.rootProject.projectDir}/LICENSE"))
    from(layout.buildDirectory.file("reports/licensee/artifacts.json").get().asFile)
    from(layout.buildDirectory.file("reports/bom.json").get().asFile)
    into(layout.buildDirectory.dir("resources/main/META-INF").get().asFile)
    rename("artifacts.json", "dependency-licenses.json")
    rename("bom.json", "SBOM.json")
    into(layout.buildDirectory.dir("resources/main/META-INF").get().asFile)
}.get()
copyLegalDocs.dependsOn(tasks.licensee)
copyLegalDocs.dependsOn(tasks.cyclonedxBom)
tasks.jar.get().dependsOn(copyLegalDocs)
tasks.javadoc.get().dependsOn(copyLegalDocs)
tasks.compileTestKotlin.get().dependsOn(copyLegalDocs)
tasks.pluginUnderTestMetadata.get().dependsOn(copyLegalDocs)
tasks.processResources.get().finalizedBy(copyLegalDocs)

tasks.test {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/nagyesta/run-dash-license-tool-gradle-plugin")
            credentials {
                username = project.properties["githubUser"]?.toString()
                password = project.properties["githubToken"]?.toString()
            }
        }
    }

    tasks.withType(GenerateModuleMetadata::class.java) {
        enabled = false
    }
}

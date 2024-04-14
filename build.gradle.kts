/*
 * SPDX-License-Identifier: MIT
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version libs.versions.kotlin.get()
    `maven-publish`
    alias(libs.plugins.plugin.publish)
    alias(libs.plugins.versioner)
    alias(libs.plugins.index.scan)
    alias(libs.plugins.owasp.dependencycheck)
}

group = "com.github.nagyesta"

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
    withType<KotlinCompile> { kotlinOptions { jvmTarget = "17" } }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.jupiter.core)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(gradleTestKit())
}

gradlePlugin {
    website.set("https://github.com/nagyesta/run-dash-license-tool-gradle-plugin")
    vcsUrl.set("https://github.com/nagyesta/run-dash-license-tool-gradle-plugin")
    plugins {
        create("runDashLicenseToolPlugin") {
            displayName = "Run Dash License Tool Gradle Plugin"
            description = "Adds a convenient task to run the Run Dash License Tool on your Gradle build"
            id = "com.github.nagyesta.run-dash-license-tool-gradle-plugin"
            implementationClass = "com.github.nagyesta.rundash.gradle.RunDashLicenseToolPlugin"
            tags.set(listOf("dash-license-tool", "license-check", "license", "dash", "run-dash"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
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

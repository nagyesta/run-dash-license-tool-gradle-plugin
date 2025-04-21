/*
 * SPDX-License-Identifier: MIT
 */

package com.github.nagyesta.rundash.gradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class RunDashLicenseToolPluginTest {

    @ParameterizedTest
    @ValueSource(strings = ["gradle-tests/minimal-kts", "gradle-tests/minimal-groovy"])
    fun testApplyShouldApplyPluginAndDoConfigWhenCalledWithAutoConfiguration(path: String) {
        //given

        //when
        val result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(File(path))
            .withArguments("clean", "runDashLicenseTool", "--stacktrace")
            .forwardOutput()
            .build()

        //then
        val output = result.output
        assertTrue(output.contains("downloadDashLicenseTool"))
        assertTrue(output.contains("listDepsForDashLicenseTool"))
        assertTrue(output.contains("runDashLicenseTool"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":runDashLicenseTool")?.outcome)
        assertValidFile("$path/build/dash-license/dependency-list.txt")
        assertValidFile("$path/build/reports/dash-license/DEPENDENCIES")
    }

    @ParameterizedTest
    @ValueSource(strings = ["gradle-tests/minimal-kts-explicit", "gradle-tests/minimal-groovy-explicit"])
    fun testApplyShouldApplyPluginAndDoConfigWhenCalledWithExplicitConfiguration(path: String) {
        //given

        //when
        val result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(File(path))
            .withArguments("clean", "runDashLicenseTool", "--stacktrace")
            .forwardOutput()
            .build()

        //then
        val output = result.output
        assertTrue(output.contains("downloadDashLicenseTool"))
        assertTrue(output.contains("listDepsForDashLicenseTool"))
        assertTrue(output.contains("runDashLicenseTool"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":runDashLicenseTool")?.outcome)
        assertValidFileWithTests("$path/build/dash-license/dependency-list.txt")
        assertValidFileWithTests("$path/build/dash-license/DEPENDENCIES")
    }

    private fun assertValidFile(path: String) {
        val file = File(path)
        assertTrue(file.exists())
        val lines = file.readLines()
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.apache.commons[:/]commons-lang3[:/]3.+$".toRegex())
        })
        assertEquals(1, lines.size)
    }

    private fun assertValidFileWithTests(path: String) {
        val file = File(path)
        assertTrue(file.exists())
        val lines = file.readLines()
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.apache.commons[:/]commons-lang3[:/]3.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.opentest4j[:/]opentest4j[:/]1.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.jupiter[:/]junit-jupiter[:/]5.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.platform[:/]junit-platform-commons[:/]1.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.platform[:/]junit-platform-engine[:/]1.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.platform[:/]junit-platform-launcher[:/]1.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.jupiter[:/]junit-jupiter-api[:/]5.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.jupiter[:/]junit-jupiter-engine[:/]5.+$".toRegex())
        })
        assertTrue(lines.stream().anyMatch {
            it.matches("^(maven/mavencentral/)?org.junit.jupiter[:/]junit-jupiter-params[:/]5.+$".toRegex())
        })
        assertEquals(9, lines.size)
    }
}

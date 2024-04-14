/*
 * SPDX-License-Identifier: MIT
 */

package com.github.nagyesta.rundash.gradle

import com.github.nagyesta.rundash.gradle.RunDashLicenseToolConfig.Companion.DEFAULT_VERSION
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.JavaExec
import java.io.File
import java.net.URL
import java.util.*
import javax.inject.Inject

open class RunDashLicenseToolPlugin @Inject constructor(project: Project) : Plugin<Project> {

    companion object Constants {
        const val EXTENSION_NAME = "runDashLicenseTool"
        const val DEPENDENCY_DOWNLOAD_TASK_NAME = "downloadDashLicenseTool"
        const val DEPENDENCY_REPORT_TASK_NAME = "listDepsForDashLicenseTool"
        const val RUN_DASH_TASK_NAME = "runDashLicenseTool"
        const val DEPENDENCY_REPORT_OUTPUT = "dash-license/dependency-list.txt"
    }

    override fun apply(project: Project) {
        project.extensions.create(EXTENSION_NAME, RunDashLicenseToolConfig::class.java, project)
        project.afterEvaluate(this@RunDashLicenseToolPlugin::afterEvaluateHandler)
    }

    private fun afterEvaluateHandler(project: Project) {
        val config = project.extensions
            .findByType(RunDashLicenseToolConfig::class.java) ?: RunDashLicenseToolConfig(project)
        val downloadTask = defineDownloadTask(config, project)
        val dependencyReportTask = defineDependencyReportTask(config, project)
        val runDashTask = defineRunDashTask(config, project)
        runDashTask.dependsOn(dependencyReportTask)
        runDashTask.dependsOn(downloadTask)
    }

    private fun defineDownloadTask(
        config: RunDashLicenseToolConfig,
        project: Project
    ): DefaultTask {
        return project.tasks.create(DEPENDENCY_DOWNLOAD_TASK_NAME, DefaultTask::class.java) { task ->
            val dashJar = getDashJar(config, project)
            task.outputs.file(dashJar)
            task.doLast {
                val url = getDashUrl(config.toolVersion)
                url.openStream().use { inputStream ->
                    dashJar.outputStream().buffered().use { out ->
                        inputStream.copyTo(out)
                    }
                }
            }
        }
    }

    private fun getDashUrl(toolVersion: String): URL {
        return if (toolVersion == DEFAULT_VERSION) {
            URL(
                "https://repo.eclipse.org/service/local/artifact/maven/redirect"
                        + "?r=dash-licenses&g=org.eclipse.dash&a=org.eclipse.dash.licenses&v=LATEST"
            )
        } else {
            URL(
                "https://repo.eclipse.org/content/repositories/dash-licenses-releases/"
                        + "org/eclipse/dash/org.eclipse.dash.licenses/"
                        + "$toolVersion/org.eclipse.dash.licenses-$toolVersion.jar"
            )
        }
    }

    private fun defineDependencyReportTask(
        config: RunDashLicenseToolConfig,
        project: Project
    ): DefaultTask {
        return project.tasks.create(DEPENDENCY_REPORT_TASK_NAME, DefaultTask::class.java) { task ->
            val reportFile = getReportFile(project)
            task.outputs.file(reportFile)
            task.doLast {
                val dependencies = TreeSet<String>()
                val configurationsInScope = HashSet<Configuration>()
                if (config.includeTests) {
                    configurationsInScope.add(project.configurations.findByName("testRuntimeClasspath")!!)
                }
                if (config.includeProduction) {
                    configurationsInScope.add(project.configurations.findByName("runtimeClasspath")!!)
                }
                for (aConfiguration in configurationsInScope) {
                    aConfiguration.resolve()
                    aConfiguration.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                        val group = artifact.moduleVersion.id.group
                        val name = artifact.moduleVersion.id.name
                        val version = artifact.moduleVersion.id.version
                        dependencies.add("$group:$name:$version")
                    }
                }
                reportFile.writer().buffered().use { writer ->
                    dependencies.stream().forEach { dependency ->
                        writer.write("${dependency}\n")
                    }
                }
            }
        }
    }

    private fun defineRunDashTask(
        config: RunDashLicenseToolConfig,
        project: Project
    ): JavaExec {
        val summaryFile = config.summaryFile
        val reportFile = getReportFile(project)
        val dashJar = getDashJar(config, project)
        return project.tasks.create(RUN_DASH_TASK_NAME, JavaExec::class.java) { javaTask ->
            javaTask.inputs.file(reportFile)
            javaTask.inputs.file(dashJar)
            javaTask.outputs.file(summaryFile)
            javaTask.mainClass.set("org.eclipse.dash.licenses.cli.Main")
            javaTask.workingDir = project.projectDir
            javaTask.classpath = project.files(dashJar.relativeTo(project.projectDir))
            javaTask.args = listOf(
                "-summary",
                "${summaryFile.relativeTo(project.projectDir)}",
                "${reportFile.relativeTo(project.projectDir)}"
            )
            javaTask.isIgnoreExitValue = !config.failOnError
            redirectLogs(javaTask)
        }
    }

    private fun getReportFile(project: Project): File {
        return project.layout.buildDirectory.file(DEPENDENCY_REPORT_OUTPUT).get().asFile
    }

    private fun getDashJar(
        config: RunDashLicenseToolConfig,
        project: Project
    ): File {
        if (config.toolVersion == DEFAULT_VERSION) {
            return project.layout.buildDirectory
                .file("dash-license/org.eclipse.dash.licenses.jar").get().asFile
        }
        return project.layout.buildDirectory
            .file("dash-license/org.eclipse.dash.licenses-${config.toolVersion}.jar").get().asFile
    }

    private fun redirectLogs(task: DefaultTask) {
        task.logging.captureStandardOutput(LogLevel.INFO)
        task.logging.captureStandardError(LogLevel.ERROR)
    }

}

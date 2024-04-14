/*
 * SPDX-License-Identifier: MIT
 */

package com.github.nagyesta.rundash.gradle

import org.gradle.api.Project
import java.io.File

open class RunDashLicenseToolConfig(project: Project) {
    var toolVersion: String
    var includeProduction: Boolean
    var includeTests: Boolean
    var summaryFile: File
    var failOnError: Boolean

    companion object {
        const val DEFAULT_VERSION = "+"
        const val DEFAULT_REPORT_FILE = "reports/dash-license/DEPENDENCIES"
    }

    init {
        toolVersion = DEFAULT_VERSION
        includeProduction = true
        includeTests = false
        summaryFile = project.layout.buildDirectory.file(DEFAULT_REPORT_FILE).get().asFile
        failOnError = true
    }
}

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
    var batchSize: Int
    var clearlyDefinedUrl: String
    var confidence: Int
    var eclipseFoundationApiUrl: String
    var approvedLicensesUrl: String
    var eclipseProjectShortName: String
    var eclipseProjectRepoUrl: String
    var review: Boolean
    var timeout: Int
    var gitLabAuthenticationToken: String

    companion object {
        const val UNSPECIFIED_STRING = ""
        const val UNSPECIFIED_INT = -1
        const val DEFAULT_VERSION = "+"
        const val DEFAULT_REPORT_FILE = "reports/dash-license/DEPENDENCIES"
    }

    init {
        toolVersion = DEFAULT_VERSION
        includeProduction = true
        includeTests = false
        summaryFile = project.layout.buildDirectory.file(DEFAULT_REPORT_FILE).get().asFile
        failOnError = true
        batchSize = UNSPECIFIED_INT
        clearlyDefinedUrl = UNSPECIFIED_STRING
        confidence = UNSPECIFIED_INT
        eclipseFoundationApiUrl = UNSPECIFIED_STRING
        approvedLicensesUrl = UNSPECIFIED_STRING
        eclipseProjectShortName = UNSPECIFIED_STRING
        eclipseProjectRepoUrl = UNSPECIFIED_STRING
        review = false
        timeout = UNSPECIFIED_INT
        gitLabAuthenticationToken = UNSPECIFIED_STRING
    }
}

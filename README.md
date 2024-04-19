# Run Dash License Tool Gradle Plugin

[![GitHub license](https://img.shields.io/github/license/nagyesta/run-dash-license-tool-gradle-plugin?color=informational)](https://raw.githubusercontent.com/nagyesta/run-dash-license-tool-gradle-plugin/main/LICENSE)
[![Java version](https://img.shields.io/badge/Java%20version-17-yellow?logo=java)](https://img.shields.io/badge/Java%20version-17-yellow?logo=java)
[![latest-release](https://img.shields.io/github/v/tag/nagyesta/run-dash-license-tool-gradle-plugin?color=blue&logo=git&label=releases&sort=semver)](https://github.com/nagyesta/run-dash-license-tool-gradle-plugin/releases)
[![Gradle Plugin](https://img.shields.io/badge/gradle-plugin-blue?logo=gradle)](https://plugins.gradle.org/plugin/io.github.nagyesta.run-dash-license-tool-gradle-plugin)
[![JavaCI](https://img.shields.io/github/actions/workflow/status/nagyesta/run-dash-license-tool-gradle-plugin/gradle.yml?logo=github&branch=main)](https://github.com/nagyesta/run-dash-license-tool-gradle-plugin/actions/workflows/gradle.yml)

[![code-climate-maintainability](https://img.shields.io/codeclimate/maintainability/nagyesta/run-dash-license-tool-gradle-plugin?logo=code%20climate)](https://img.shields.io/codeclimate/maintainability/nagyesta/run-dash-license-tool-gradle-plugin?logo=code%20climate)
[![code-climate-tech-debt](https://img.shields.io/codeclimate/tech-debt/nagyesta/run-dash-license-tool-gradle-plugin?logo=code%20climate)](https://img.shields.io/codeclimate/tech-debt/nagyesta/run-dash-license-tool-gradle-plugin?logo=code%20climate)
[![last_commit](https://img.shields.io/github/last-commit/nagyesta/run-dash-license-tool-gradle-plugin?logo=git)](https://img.shields.io/github/last-commit/nagyesta/run-dash-license-tool-gradle-plugin?logo=git)

This project provides Gradle integration to allow convenient download and execution of the 
[Eclipse Dash License Tool](https://github.com/eclipse/dash-licenses)

> [!NOTE]
> This project is not affiliated with the Eclipse Dash License Tool or The Eclipse Foundation.
> The copyright and license of the Eclipse Dash License Tool belong to their respective owners.

## Installation

### Minimal configuration

#### build.gradle

```groovy
plugins {
    id "io.github.nagyesta.run-dash-license-tool-gradle-plugin" version "<version>"
}

repositories {
    mavenCentral()
}
```

### Configuration properties

The plugin can be configured using the following DSL

#### build.gradle

```groovy
runDashLicenseTool {
    //Set the version of the Dash License Tool we want to automatically download
    //and run
    toolVersion "+"
    //Whether we should include dependencies from runtimeClasspath
    includeProduction true
    //Whether we should include dependencies from testRuntimeClasspath
    includeTests true
    //Whether we should fail the build in case the Dash License Tool exits
    //with a non-zero status code
    failOnError false
    //Sets where we want to save the summary report
    summaryFile layout.buildDirectory.file("reports/dash-license/DEPENDENCIES").get().getAsFile()
    //Defines the batch size of the Dash API calls
    batchSize 500
    //Can override the Clearly Defined API URL
    clearlyDefinedUrl "https://api.clearlydefined.io/definitions"
    //Confidence threshold expressed as integer percent (0-100)
    confidence 60
    //Can override the Eclipse Foundation API URL
    eclipseFoundationApiUrl "https://www.eclipse.org/projects/services/license_check.php"
    //Can override the URL of the approved licenses list
    approvedLicensesUrl "https://www.eclipse.org/legal/licenses.json"
    //Perform a review (must also specify the project and token
    review false
    //Sets the timeout for HTTP calls (in seconds)
    timeout 30
    //The short name of the current Eclipse Foundation project
    eclipseProjectShortName ""
    //The repository URL of the current Eclipse Foundation project
    eclipseProjectRepoUrl ""
    //The GitLab authentication token
    gitLabAuthenticationToken ""
}
```

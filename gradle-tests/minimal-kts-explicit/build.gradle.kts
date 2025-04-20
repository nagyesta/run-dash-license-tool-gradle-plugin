plugins {
    java
    id("io.github.nagyesta.run-dash-license-tool-gradle-plugin")
}

group = "com.github.nagyesta.run-dash-license-tool.test"
version = "0.0.1"

dependencies {
    @Suppress("kotlin:S6624")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    @Suppress("kotlin:S6624")
    testImplementation("org.junit.jupiter:junit-jupiter:[5.8.0,5.8.1)")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

runDashLicenseTool {
    toolVersion = "1.1.0"
    includeProduction = true
    includeTests = true
    failOnError = false
    summaryFile = layout.buildDirectory.file("dash-license/DEPENDENCIES").get().asFile
    batchSize = 500
    clearlyDefinedUrl = "https://api.clearlydefined.io/definitions"
    confidence = 60
    eclipseFoundationApiUrl = "https://www.eclipse.org/projects/services/license_check.php"
    approvedLicensesUrl = "https://www.eclipse.org/legal/licenses.json"
    review = false
    timeout = 30
    eclipseProjectShortName = ""
    eclipseProjectRepoUrl = ""
    gitLabAuthenticationToken = ""
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

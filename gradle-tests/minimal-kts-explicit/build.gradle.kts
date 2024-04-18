plugins {
    java
    id("io.github.nagyesta.run-dash-license-tool-gradle-plugin")
}

group = "com.github.nagyesta.run-dash-license-tool.test"
version = "0.0.1"

dependencies {
    implementation("org.apache.commons:commons-lang3:3.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter:[5.8.0,5.8.1)")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

runDashLicenseTool {
    toolVersion = "1.1.0"
    includeProduction = true
    includeTests = true
    failOnError = false
    summaryFile = layout.buildDirectory.file("dash-license/DEPENDENCIES").get().asFile
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

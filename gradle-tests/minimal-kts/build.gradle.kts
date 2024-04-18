plugins {
    java
    id("io.github.nagyesta.run-dash-license-tool-gradle-plugin")
}

group = "com.github.nagyesta.run-dash-license-tool.test"
version = "0.0.1"

dependencies {
    implementation("org.apache.commons:commons-lang3:+")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

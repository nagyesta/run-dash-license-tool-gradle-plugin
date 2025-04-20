plugins {
    java
    id("io.github.nagyesta.run-dash-license-tool-gradle-plugin")
}

group = "com.github.nagyesta.run-dash-license-tool.test"
version = "0.0.1"

dependencies {
    @Suppress("kotlin:S6624")
    implementation("org.apache.commons:commons-lang3:+")
    @Suppress("kotlin:S6624")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

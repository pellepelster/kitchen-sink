plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
    id("java-test-fixtures")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
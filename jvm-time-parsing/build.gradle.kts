plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("joda-time:joda-time:2.12.7")
    implementation("org.freemarker:freemarker:2.3.32")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("io.pelle.jvm.time.AppKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":model"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")

    testImplementation(testFixtures(project(":model")))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("io.pelle.kitchensink.app.AppKt")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("nu.studer.jooq") version "5.2.1"
    id("com.palantir.docker") version "0.26.0"

    kotlin("jvm") version "1.5.10"
    kotlin("plugin.spring") version "1.5.10"
}

group = "io.pelle.todo"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":todo-frontend-angular"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.jooq:jooq:3.14.11")
    implementation("org.jooq:jooq-meta-extensions-liquibase:3.14.11")

    implementation("org.postgresql:postgresql:42.2.18")

    implementation("io.swagger.core.v3:swagger-annotations:2.1.7")
    implementation("org.springdoc:springdoc-openapi:1.5.2")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.5.5")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.2")

    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")

    // snippet:gradle_jooq_extension_dependency
    jooqGenerator("org.jooq:jooq-meta-extensions-liquibase")
    jooqGenerator("org.liquibase:liquibase-core:3.10.3")
    jooqGenerator("org.yaml:snakeyaml:1.28")
    jooqGenerator("org.slf4j:slf4j-jdk14:1.7.30")
    // /snippet:gradle_jooq_extension_dependency
}

java.sourceCompatibility = JavaVersion.VERSION_13

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_13.toString()
        javaParameters = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// snippet:gradle_jooq_extension_configuration
jooq {
    version.set("3.14.11")

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"

                    target.apply {
                        packageName = "io.pelle.todo.db.generated"
                    }

                    database.apply {
                        name = "org.jooq.meta.extensions.liquibase.LiquibaseDatabase"
                        properties.add(
                                org.jooq.meta.jaxb.Property().withKey("scripts")
                                        .withValue("src/main/resources/db/changelog/db.changelog-master.yaml")
                        )

                        properties.add(
                                org.jooq.meta.jaxb.Property().withKey("includeLiquibaseTables").withValue("false")
                        )
                    }
                }
            }
        }
    }
}
// /snippet:gradle_jooq_extension_configuration

val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

docker {
    dependsOn(bootJar)
    name = project.name
    files(bootJar.outputs)
    buildArgs(mapOf(
            "JAR_FILE" to bootJar.archiveFileName.get()
    ))
}
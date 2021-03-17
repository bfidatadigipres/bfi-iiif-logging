import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import de.undercouch.gradle.tasks.download.Download
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "2.3.9.BUILD-SNAPSHOT"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    id("de.undercouch.download") version "4.1.1"
    jacoco
}

group = "uk.org.bfi"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.flywaydb:flyway-core:6.5.7")
    implementation("com.google.guava:guava:30.1-jre")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}


val runTask = tasks["bootRun"] as BootRun

jacoco {
    applyTo(runTask)
}

val runCoverageTask = tasks.register<JacocoReport>("bootRunCoverage") {
    executionData(runTask)
    sourceSets(sourceSets["main"])
}

val downloadUvBundleTask by tasks.register<Download>("downloadUvBundle") {
    src("https://cdn.jsdelivr.net/npm/universalviewer@3.1.1/dist/uv.zip")
    dest("${projectDir}/src/main/resources/static/uv.zip")
    overwrite(true);
}

val unzipUvBundleTask by tasks.register<Copy>("unzipUvBundle") {
    dependsOn(downloadUvBundleTask)
    from(zipTree(downloadUvBundleTask.dest))
    into("${projectDir}/src/main/resources/static/uv")
}

tasks {
    build {
        downloadUvBundleTask
    }

    bootRun {
        finalizedBy(runCoverageTask)
    }
}

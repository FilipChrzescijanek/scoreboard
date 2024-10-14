plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
                         "Implementation-Version" to project.version))
    }
}

group = "io.github.filipchrzescijanek"
version = "0.0.1"

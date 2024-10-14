plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":scoreboard-core"))
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "io.github.filipchrzescijanek.App" 
}

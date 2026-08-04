plugins {
    id("org.springframework.boot") version "4.1.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

group = "io.jatinjindal"
version = "1.0.0-SNAPSHOT"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    pluginManager.withPlugin("java") {
        dependencies {
            add("testImplementation", platform("org.junit:junit-bom:5.13.4"))
            add("testImplementation", "org.junit.jupiter:junit-jupiter")
            add("testImplementation", "org.mockito:mockito-junit-jupiter:5.18.0")
            add("testImplementation", "org.assertj:assertj-core:3.27.7")
        }

        tasks.withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}
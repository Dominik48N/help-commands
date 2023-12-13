plugins {
    id("java")
    id("net.kyori.blossom") version "2.1.0"
}

group = "com.github.dominik48n"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Velocity
    compileOnly("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.2.0-SNAPSHOT")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

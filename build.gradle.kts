import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.github.shaksternano"
base.archivesName.set("pdf-creator")
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.pdfbox:pdfbox:3.0.0-alpha3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("pdf-creator-shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "${project.group}.pdfcreator.Main"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

task("copyToLib") {
    doLast {
        copy {
            into("$buildDir/libs")
            from(configurations.compileClasspath)
        }
    }
}

task("stage") {
    dependsOn.add("build")
    dependsOn.add("copyToLib")
}

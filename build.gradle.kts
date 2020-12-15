plugins {

    val kotlinVersion = "1.4.20"

    application
    kotlin("jvm") version kotlinVersion
}

application {
    mainClass.set("k4jd.wscrambler.kotlin.WordScrambler")
}

group = "k4jd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.google.cloud:google-cloud-texttospeech:1.2.4")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "15"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "15"
    }
}

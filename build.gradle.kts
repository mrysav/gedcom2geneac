plugins {
    java
    application
    kotlin("jvm") version "1.3.61"
}

group = "me.rysavys.geneac"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "me.rysavys.geneac.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.familysearch.gedcom", "gedcom", "1.10.0")
    implementation("org.slf4j", "slf4j-api", "1.7.28")
    implementation("org.slf4j", "slf4j-simple", "1.7.28")
    implementation("com.google.code.gson", "gson", "2.8.6")
    implementation("com.github.ajalt", "clikt", "2.3.0")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

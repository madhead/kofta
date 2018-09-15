plugins {
    kotlin("jvm").version("1.2.70")
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(group = "com.fasterxml.jackson", name = "jackson-bom", version = "2.9.6")

    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.2.70")
    implementation(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin")
    implementation(group = "org.apache.kafka", name = "kafka-clients", version = "2.0.0")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.3.0")
    testRuntime(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.3.0")
}

application {
    mainClassName = "by.dev.madhead.kofta.Kofta"
}

tasks {
    val wrapper by creating(Wrapper::class) {
        gradleVersion = "4.10.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    val test by getting(Test::class) {
        useJUnitPlatform()
    }

    withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

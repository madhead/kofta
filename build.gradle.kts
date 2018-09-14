plugins {
    kotlin("jvm").version("1.2.70")
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.2.70"))
}

application {
    mainClassName = "by.dev.madhead.kofta.Kofta"
}

tasks {
    val wrapper by creating(Wrapper::class) {
        gradleVersion = "4.10.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

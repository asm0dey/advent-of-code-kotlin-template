plugins {
    kotlin("jvm") version "2.1.0"
    java
}

sourceSets {
    main {
        kotlin.srcDir("src")
        java.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
        vendor.set(JvmVendorSpec.BELLSOFT)

    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(listOf("--enable-preview", "-Xlint:preview"))
}

tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview", "-ea")
}

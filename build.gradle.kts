plugins {
    application
    antlr
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.antlr:antlr4-runtime:4.9.2")
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("com.google.code.gson:gson:2.8.6")
    antlr("com.ibm.icu:icu4j:69.1")
    antlr("org.antlr:antlr4:4.9.2") {
        exclude(group = "org.abego.treelayout")
    }
}

tasks.wrapper {
    distributionSha256Sum = "81003f83b0056d20eedf48cddd4f52a9813163d4ba185bcf8abd34b8eeea4cbd"
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    if (JavaVersion.current().isJava9Compatible) {
        // TODO: add a "release" option
    }
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.withType<AntlrTask> {
    arguments = arguments + listOf("-package", "com.github.kb1000.jypy.parser.antlr", "-visitor", "-listener", "-lib", file("src/main/antlr").absolutePath)
    outputDirectory = File(File(File(File(File(File(outputDirectory, "com"), "github"), "kb1000"), "jypy"), "parser"), "antlr")
}

tasks.processResources {
    exclude("**/*.pyc")
    exclude("**/__pycache__")
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_jypy"}
    }
}

application.mainClass.set("com.github.kb1000.jypy.Main")

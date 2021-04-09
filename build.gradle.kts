plugins {
    application
    antlr
}

dependencies {
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.antlr:antlr4-runtime:4.7")
    implementation("org.jetbrains:annotations:20.1.0")
    antlr("com.ibm.icu:icu4j:66.1")
    antlr("org.antlr:antlr4:4.7") {
        exclude(group = "org.abego.treelayout")
    }
}

repositories {
    mavenCentral()
}

tasks {
    wrapper {
        distributionSha256Sum = "9af5c8e7e2cd1a3b0f694a4ac262b9f38c75262e74a9e8b5101af302a6beadd7"
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = "6.8.3"
    }
}

tasks.withType<JavaCompile> {
    options.headerOutputDirectory.set(project.projectDir)
}

tasks.withType<AntlrTask> {
    arguments = arguments + listOf("-package", "com.github.kb1000.jypy.parser.antlr", "-visitor", "-listener")
}

application.mainClassName = "com.github.kb1000.jypy.Main"

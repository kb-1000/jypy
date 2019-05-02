plugins {
    application
    antlr
}

dependencies {
    implementation("org.ow2.asm:asm:7.1")
    implementation("org.antlr:antlr4-runtime:4.7")
    antlr("org.antlr:antlr4:4.7")
}

repositories {
    jcenter()
}

tasks {
    wrapper {
        distributionSha256Sum = "7bdbad1e4f54f13c8a78abc00c26d44dd8709d4aedb704d913fb1bb78ac025dc"
        distributionType = Wrapper.DistributionType.BIN
        gradleVersion = "5.4.1"
    }
}

tasks.withType<JavaCompile> {
    options.headerOutputDirectory.set(project.projectDir)
}

tasks.withType<AntlrTask> {
    arguments = arguments + listOf("-package", "com.github.kb1000.jypy.parser.antlr", "-visitor", "-listener")
}

application.mainClassName = "com.github.kb1000.jypy.Main"

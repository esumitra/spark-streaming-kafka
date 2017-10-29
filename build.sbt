import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "spark-streams",
    libraryDependencies ++= Dependencies.core ++ Dependencies.scalaSpecs,
    mainClass in assembly := Some("example.KafkaStream"),
    assemblyJarName in assembly := "spark-streams.jar",
    test in assembly := {},
    // ignore lib refs in jars
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

import sbt._

object Dependencies {
  lazy val scalaSpecs = Seq(
    "org.specs2" %% "specs2-core" % "3.9.5" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
  )

  lazy val core = Seq(
    "org.apache.spark" %% "spark-core" % "2.2.0" % "provided",
    "org.apache.spark" %% "spark-sql" % "2.2.0" % "provided",
    "org.apache.spark" %% "spark-streaming" % "2.2.0" % "provided",
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.2.0",
    "com.typesafe" % "config" % "1.3.1"
  )
}

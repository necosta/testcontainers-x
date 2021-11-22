import sbt._

object Dependencies {

  private val ScalaTestVersion = "3.2.9"
  private val TestContainersVersion = "0.39.12"
  private val Log4CatsVersion = "2.1.1"
  private val CatsSpecs2Version = "1.3.0"
  private val Fs2KafkaVersion = "2.2.0"

  val libs = Seq(
    "com.github.fd4s" %% "fs2-kafka" % Fs2KafkaVersion,
    "com.github.fd4s" %% "fs2-kafka-vulcan" % Fs2KafkaVersion
  )

  val testLibs = Seq(
    "com.dimafeng" %% "testcontainers-scala" % TestContainersVersion % "test,it",
    "com.dimafeng" %% "testcontainers-scala-kafka" % TestContainersVersion % "test,it",
    "org.scalatest" %% "scalatest" % ScalaTestVersion % "test,it",
    "org.typelevel" %% "cats-effect-testing-specs2" % CatsSpecs2Version % "test,it",
    "org.typelevel" %% "log4cats-testing" % Log4CatsVersion % "test,it"
  )
}

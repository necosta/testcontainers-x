import sbt._

object Dependencies {

  private val TestContainersVersion = "0.39.12"
  private val Log4CatsVersion = "2.1.1"
  private val CatsSpecs2Version = "1.3.0"

  val libs = Seq(
    "com.dimafeng" %% "testcontainers-scala-kafka" % TestContainersVersion
  )

  val testLibs = Seq(
    "org.typelevel" %% "cats-effect-testing-specs2" % CatsSpecs2Version % "test,it",
    "org.typelevel" %% "log4cats-testing" % Log4CatsVersion % "test,it"
  )
}

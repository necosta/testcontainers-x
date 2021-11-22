import Dependencies._

val Scala2 = "2.13.6"

resolvers += "confluent" at "https://packages.confluent.io/maven/"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    organization := "com.necosta",
    name := "testcontainers-x",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := Scala2,
    Defaults.itSettings,
    libraryDependencies ++= libs ++ testLibs
  )

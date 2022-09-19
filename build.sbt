import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "ChEBIOWLAPI",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "utest" % "0.8.1" % "test",
      "com.github.p2m2" %% "discovery" % "0.4.0"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
Global / onChangedBuildSource := ReloadOnSourceChanges
name := "Story2Sound"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "uk.co.panaxiom" %% "play-jongo" % "0.7.1-jongo1.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.1",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)



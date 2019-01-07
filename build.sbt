import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "akka-streams-tutorial",
    libraryDependencies += scalaTest % Test, 
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.19"
  )

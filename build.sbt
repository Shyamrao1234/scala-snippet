//ThisBuild / version := "0.1.0-SNAPSHOT"
//
//ThisBuild / scalaVersion := "2.13.16"
//
//lazy val root = (project in file("."))
//  .settings(
//    name := "scala-snippet"
//  )

name := "hello-akka"

version := "1.0"

scalaVersion := "2.13.16"

// Define a consistent Akka version
val AkkaVersion = "2.6.20"
val AkkaHttpVersion = "10.2.10" // Match this to Akka 2.6

// Akka core modules
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"       % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream"      % AkkaVersion,
  "com.typesafe.akka" %% "akka-http"        % AkkaHttpVersion
)

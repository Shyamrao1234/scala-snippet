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

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.20"
libraryDependencies +=   "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20"


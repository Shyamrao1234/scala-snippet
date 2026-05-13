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
scalaVersion := "2.12.14"

val akkaStreamKafkaVersion = "2.0.4"
val alpakkaMqttVersion     = "2.0.1"
val akkaHttpVersion        = "10.2.10"
val akkaTypedVersion       = "2.6.20"
val catsVersion            = "2.1.1"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaTypedVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaTypedVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaTypedVersion % Test

val jodaDateTime = "joda-time" % "joda-time" % "2.9.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % akkaTypedVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "com.typesafe.akka" %% "akka-stream" % akkaTypedVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaTypedVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaTypedVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % alpakkaMqttVersion,
  jodaDateTime,
  //akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "org.scalatest" %% "scalatest" % "3.2.17",


  //cats library
  "org.typelevel" %% "cats-core" % catsVersion,


  //akka persistence
  "com.typesafe.akka" %% "akka-persistence-typed" % akkaTypedVersion,
  // Robust serialization for Events and State (Production Standard)
  "com.typesafe.akka" %% "akka-serialization-jackson" % akkaTypedVersion,

  // In-memory journal for local testing
  "com.typesafe.akka" %% "akka-persistence-testkit" % akkaTypedVersion % Test,

  // Gatling Load Testing Libraries (Version 3.4.2 is the last version supporting Scala 2.12)
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.4.2" % Test,
  "io.gatling"            % "gatling-test-framework"    % "3.4.2" % Test

  //"org.apache.kafka" %% "kafka" % "3.6.0", // Core Kafka
  //"org.apache.kafka" % "kafka-clients" % "3.6.0" // Kafka client library
)
val jacksonVersion         = "2.11.4" // Added for the Jackson fix
dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
)
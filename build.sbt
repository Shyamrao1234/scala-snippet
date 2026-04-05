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


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaTypedVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaTypedVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaTypedVersion % Test
addSbtPlugin("io.gatling" % "gatling-sbt" % "4.5.0")
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



  //"org.apache.kafka" %% "kafka" % "3.6.0", // Core Kafka
  //"org.apache.kafka" % "kafka-clients" % "3.6.0" // Kafka client library
)
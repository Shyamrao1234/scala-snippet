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


val akkaStreamKafkaVersion="2.0.4"
val alpakkaMqttVersion="2.0.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.20"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.20",
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "com.typesafe.akka" %% "akka-stream"% "2.6.20",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.20",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.20",
  "com.typesafe.akka" %% "akka-http"% "10.2.10",
  "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % alpakkaMqttVersion
//"org.apache.kafka" %% "kafka" % "3.6.0", // Core Kafka
//"org.apache.kafka" % "kafka-clients" % "3.6.0" // Kafka client library
)


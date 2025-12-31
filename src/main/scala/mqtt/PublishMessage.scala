package mqtt

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.mqtt.MqttQoS.AtLeastOnce
import akka.stream.alpakka.mqtt.scaladsl.MqttSink
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttQoS}
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import scala.concurrent.ExecutionContext.Implicits.global
import javax.net.ssl.SSLContext
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object PublishMessage extends App{

  implicit  val actorSystem = ActorSystem("Mqtt-Publisher")

  val connectionSetting = MqttConnectionSettings(
    "tcp://test.mosquitto.org:1883",
    "Unique@1342dhhrt-etr",
    new MemoryPersistence()
  )


  val sink:Sink[MqttMessage,Future[Done]] = MqttSink(connectionSetting,AtLeastOnce)

  val mqttMessage = MqttMessage("test/123/shyam",ByteString("Hello I am new to mqtt"))
    .withQos(MqttQoS.AtLeastOnce)
    .withRetained(true)


  val res = Source.tick(0.seconds,1.seconds,()).map{ _ =>
    mqttMessage
  }.runWith(sink)

  res.onComplete {
    case Failure(exception) => {
      println("Message published successfully")
      actorSystem.terminate()
    }
    case Success(value) =>
      println(s"Failed to publish message ${value}")
      actorSystem.terminate()
  }



}

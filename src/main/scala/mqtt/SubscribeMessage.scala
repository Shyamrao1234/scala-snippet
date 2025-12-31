package mqtt

import akka.Done
import akka.stream.alpakka.mqtt.MqttQoS.AtLeastOnce
import akka.stream.alpakka.mqtt.scaladsl.MqttSource
import akka.stream.alpakka.mqtt.{MqttConnectionSettings, MqttMessage, MqttSubscriptions}
import akka.stream.scaladsl.Source
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

import java.net.{InetSocketAddress, Socket}
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}

object SubscribeMessage extends App{

//  val connectionSetting=MqttConnectionSettings("tcp//:test.mosquitto.org:1883","test-2321111-33",new MemoryPersistence)
//
//  val mqttSource:Source[MqttMessage,Future[Done]] =
//    MqttSource.atMostOnce(
//      connectionSetting,
//      MqttSubscriptions("test/123/shyam",AtLeastOnce),
//      bufferSize = 8
//    )

  private def isMqttServerAccessible(host:String,
                                     port:Int,
                                     timeout:FiniteDuration=5.seconds
                                    )={
    val socket=new Socket()

    try{
      socket.connect(new InetSocketAddress(host,port),timeout.toMillis.toInt)
      true
    }catch{
      case _:Exception => false
    }finally {
      try socket.close()
      catch {
        case _:Exception=>
      }
    }
  }

  println(isMqttServerAccessible("test.mosquitto.org",1883))



}

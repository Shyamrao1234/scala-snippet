package akkatypedactors

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable

sealed trait Command
case class UpdateDevice(device:Device) extends Command
case class GetDevice(deviceId:String,replyTo:ActorRef[DeviceResponse]) extends Command

sealed trait DeviceResponse
case class DeviceFound(device:Device) extends DeviceResponse
case class DeviceNotFound(id:String) extends DeviceResponse


object UpdatingMapWithActors{

}

object UpdateCache {
  val map=mutable.Map.empty[String,Device]

  def apply():Behavior[Command] = Behaviors.receive {(context,message)=>

    message match {
      case UpdateDevice(device) =>
        context.log.info(s"Updating Device : id: ${device.id} name : ${device.name}")
        map.put(device.id,device)
        Behaviors.same

      case GetDevice(id,replyTo)=>
        context.log.info(s"GetDevice :id: ${id}")
        map.get(id) match {
          case Some(value) => replyTo ! DeviceFound(value)
          case None => replyTo ! DeviceNotFound(id)
        }
        Behaviors.same
    }
  }
}

object ClientActor {

  sealed trait ClientCommand
  final case class Put(device: Device) extends ClientCommand
  final case class Fetch(id: String) extends ClientCommand
  final case class Wrapped(res: DeviceResponse) extends ClientCommand

  def apply(cache: ActorRef[Command]): Behavior[ClientCommand] =
    Behaviors.setup { context =>

      // Adapter for async response from cache actor
      val adapter: ActorRef[DeviceResponse] =
        context.messageAdapter(res => Wrapped(res))

      Behaviors.receiveMessage {

        //-----------------------------------------
        // PUT device
        //-----------------------------------------
        case Put(device) =>
          cache ! UpdateDevice(device)
          context.log.info(s"[Client] Requested PUT for ${device.id}")
          Behaviors.same

        //-----------------------------------------
        // GET device
        //-----------------------------------------
        case Fetch(id) =>
          cache ! GetDevice(id, adapter)
          context.log.info(s"[Client] Requested GET for $id")
          Behaviors.same

        //-----------------------------------------
        // Response from Cache
        //-----------------------------------------
        case Wrapped(res) =>
          res match {
            case DeviceFound(device) =>
              context.log.info(s"[Client] Device found: $device")

            case DeviceNotFound(id) =>
              context.log.info(s"[Client] Device NOT found: $id")
          }
          Behaviors.same
      }
    }
}

object Main extends App {
  val system  = ActorSystem(Behaviors.empty, "Root")

  val cache  = system.systemActorOf(UpdateCache(),  "cache")
  val client = system.systemActorOf(ClientActor(cache), "client")

  // THREAD 1 → PUT
  new Thread(() => {
    client ! ClientActor.Put(Device("101","Mobile"))
  }).start()

  // THREAD 2 → GET
  new Thread(() => {
    Thread.sleep(500)  // ensure PUT happens first
    client ! ClientActor.Fetch("101")
  }).start()
}

case class Device(id:String,name:String)
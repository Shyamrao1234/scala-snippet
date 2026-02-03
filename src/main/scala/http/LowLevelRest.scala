package http

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import http.GuitarDB._
//import http.GuitarDB.{CreateGuitar, FindAllGuitar}
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

//case class Guitar(make: String, model: String)
//
//object GuitarDB {
//  case class CreateGuitar(guitar: Guitar)
//  case class GuitarCreated(id: Int)
//  case class FindGuitar(id: Int)
//  case object FindAllGuitar
//}
//
//class GuitarDB extends Actor with ActorLogging {
//
//  import GuitarDB._
//
//  var guitars: mutable.Map[Int, Guitar] = scala.collection.mutable.Map()
//  var currentGuitarId: Int = 0
//
//
//  override def receive: Receive = {
//    case FindAllGuitar =>
//      log.info("Searching all Guitar")
//      sender() ! guitars.values.toList
//    case FindGuitar(id) =>
//      log.info(s"Searching guitar by id : ${id}")
//      sender() ! guitars.get(id)
//    case CreateGuitar(guitar) =>
//      log.info(s"Adding guitar ${guitar} with id $currentGuitarId")
//      guitars = guitars + (currentGuitarId -> guitar)
//      sender() ! GuitarCreated(currentGuitarId)
//      currentGuitarId += 1
//  }
//}
//
//// Object - JSON is marshalling
//// JSON - object is unmarshalling
//
//trait GuitarJsonProtocol extends DefaultJsonProtocol {
//  implicit val format = jsonFormat2(Guitar)
//}
//
//object LowLevelRest extends App with GuitarJsonProtocol {
//
//  implicit val system = ActorSystem("LowLevelRest")
//  implicit val materializer = ActorMaterializer()
//
//
//  val guitarActor = system.actorOf(Props[GuitarDB], "lowlevelApi")
//  val listOfGuitar = List(
//    Guitar("A", "33"),
//    Guitar("B", "22"),
//    Guitar("C", "45")
//  )
//  listOfGuitar.foreach { guitar =>
//    guitarActor ! CreateGuitar(guitar)
//  }
//
//  implicit val timeout = Timeout(3.seconds)
//  val requestHandler: HttpRequest => Future[HttpResponse] = {
//    case HttpRequest(HttpMethods.GET, Uri.Path("/api/guitar"), _, _, _) =>
//      val getGuitars = (guitarActor ? FindAllGuitar).mapTo[List[Guitar]]
//      getGuitars.map { guitars =>
//        HttpResponse(StatusCodes.OK,
//          entity = HttpEntity(ContentTypes.`application/json`, guitars.toJson.prettyPrint)
//        )
//      }
//
//    case request: HttpRequest =>
//      request.discardEntityBytes()
//      Future {
//        HttpResponse(StatusCodes.NotFound)
//      }
//  }
//
//
//  Http().bindAndHandleAsync(requestHandler, "localhost", 8080)
//
//
//}


case class Guitar(name: String, model: String, quantity: Int = 0)

object GuitarDB {
  case object FindAllGuitar

  case class FindGuitarById(id: Int)

  case class CreateGuitar(guitar: Guitar)

  case class GuitarCreated(id: Int)

  case class AddQuantity(id: Int, quantity: Int)
}


class GuitarDB extends Actor with ActorLogging {

  var guitarList = scala.collection.mutable.Map[Int, Guitar]()
  var currentGuitarId: Int = 0

  override def receive: Receive = {
    case FindAllGuitar =>
      log.info("Find All Guitar")
      sender() ! guitarList.values.toList
    case CreateGuitar(guitar) =>
      log.info(s"Guitar created with id : ${currentGuitarId}")
      guitarList = guitarList + (currentGuitarId -> guitar)
      sender() ! GuitarCreated(currentGuitarId)
      currentGuitarId += 1
    case FindGuitarById(id) =>
      log.info(s"Finding Guitar by id: $id")
      sender() ! guitarList.get(id)
    case AddQuantity(id, quantity) =>
      log.info("Adding quantity")
      val findGuitar = guitarList.get(id)
      val newGuitarOpt = findGuitar.map {
        case Guitar(name, model, q) => Guitar(name, model, q + quantity)
      }
      newGuitarOpt.map { guitar => guitarList += (id -> guitar) }
      sender() ! newGuitarOpt
  }
}

trait GuitarJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat3(Guitar)
}

object LowLevelRest extends App with GuitarJsonProtocol {

  implicit val system = ActorSystem("LowLevelRest")
  implicit val materializer = ActorMaterializer()


  val guitarActor = system.actorOf(Props[GuitarDB], "GuitarDB")
  val listOfGuitar = List(
    Guitar("Guitar-1", "200"),
    Guitar("Guitar-2", "444"),
    Guitar("Guitar-3", "6666")
  )


  listOfGuitar.foreach { guitar =>
    guitarActor ! CreateGuitar(guitar)
  }


  implicit val timeout = Timeout(2.seconds)

  def getGuitar(query: Query): Future[HttpResponse] = {
    val getIdOpt = query.get("id").map(_.toInt)
    getIdOpt match {
      case None => Future(HttpResponse(StatusCodes.NotFound))
      case Some(id) =>
        val askToDb = (guitarActor ? FindGuitarById(id)).mapTo[Option[Guitar]]
        askToDb.map {
          case None => HttpResponse(StatusCodes.NotFound)
          case Some(guitar) => HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, guitar.toJson.prettyPrint))
        }
    }
  }


  val requestHandler: HttpRequest => Future[HttpResponse] = {
    case HttpRequest(HttpMethods.GET, uri@Uri.Path("/api/guitar"), _, _, _) =>
      val query = uri.query()
      if (query.isEmpty) {
        val getGuitars = (guitarActor ? FindAllGuitar).mapTo[List[Guitar]]
        getGuitars.map { guitars =>
          HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, guitars.toJson.prettyPrint))
        }
      } else {
        getGuitar(query)
      }

    case HttpRequest(HttpMethods.POST, uri@Uri.Path("/api/guitar/inventory"), _, _, _) =>
      val query = uri.query()
      val idOpt = query.get("id").map(_.toInt)
      val quantityOpt = query.get("quantity").map(_.toInt)
      val update = for {
        id <- idOpt
        quantity <- quantityOpt
      } yield {
        val askActorToUpdate = (guitarActor ? AddQuantity(id, quantity)).mapTo[Option[Guitar]]
        askActorToUpdate.map { _ =>
          HttpResponse(StatusCodes.OK)
        }
      }
      update.getOrElse(Future(HttpResponse(StatusCodes.NotFound)))

    //using strict
    case HttpRequest(HttpMethods.POST, Uri.Path("/api/guitar"), _, entity, _) =>
      val strictEntityFuture = entity.toStrict(3.seconds)
      strictEntityFuture.flatMap { strictEntity =>
        val guitarJsonString = strictEntity.data.utf8String
        val guitar = guitarJsonString.parseJson.convertTo[Guitar]
        val askActor = (guitarActor ? CreateGuitar(guitar)).mapTo[GuitarCreated]
        askActor.map { _ =>
          HttpResponse(StatusCodes.OK)
        }
      }

    case HttpRequest(HttpMethods.POST, Uri.Path("/api/stream/guitar"), _, entity, _) =>
      val entityStream = entity.dataBytes.map(_.utf8String).runFold("")(_ ++ _)
      entityStream.flatMap { guitarJson =>
        val guitar = guitarJson.parseJson.convertTo[Guitar]
        println(" guitar " + guitar)
        val askActorToPush = (guitarActor ? CreateGuitar(guitar)).mapTo[GuitarCreated]
        askActorToPush.map { guitarCreated =>
          HttpResponse(StatusCodes.OK)
        }
      }


    case request: HttpRequest =>
      request.discardEntityBytes()
      Future(HttpResponse(StatusCodes.NotFound))
  }

  Http().bindAndHandleAsync(requestHandler, "localhost", 8080)


  /** *
   * - Exercise add new field called quantity
   * -
   */


}

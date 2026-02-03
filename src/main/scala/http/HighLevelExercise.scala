package http

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.stream.ActorMaterializer
import http.GuitarDB._
import spray.json._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt

trait HighLevelJsonProtocol extends DefaultJsonProtocol {
  implicit val jsonFormat = jsonFormat3(Guitar)
}


object HighLevelExercise extends App with HighLevelJsonProtocol {


  implicit val system = ActorSystem("HighLevelObject")
  implicit val materiaizer = ActorMaterializer()

  import system.dispatcher
  import akka.http.scaladsl.server.Directives._

  /** *
   * GET /api/guitar fetch all the guitars in the store
   * GET /api/guitar?id=x fetch the guitar with id x
   * GET /api/guitar/X fetches guitar with id x
   * GET /api/guitar/Inventory?inStock=true
   */

  val guitarDB = system.actorOf(Props[GuitarDB], "HighLevel")
  val guitarList = List(
    Guitar("Fender", "Stratocaster"),
    Guitar("Gibson", "Les Paul"),
    Guitar("Martin", "LX1")
  )
  guitarList.foreach { guitar =>
    guitarDB ! CreateGuitar(guitar)
  }

  implicit val timeout: Timeout = Timeout(2.seconds)

  def toHttpEntity(json: String) = HttpEntity(ContentTypes.`application/json`, json)

  val guitarServerRoute =
    path("api" / "guitar") {
      parameter("id".as[Int]) { id =>
        get {
          val guitarFuture = (guitarDB ? FindGuitarById(id)).mapTo[Option[Guitar]].map(_.toJson.prettyPrint).map(toHttpEntity)
          complete(guitarFuture)
        }
      } ~
        get {
          val guitarListFuture = (guitarDB ? FindAllGuitar).mapTo[List[Guitar]].map(_.toJson.prettyPrint).map(toHttpEntity)
          complete(guitarListFuture)
        }
    } ~ path("api" / "guitar" / IntNumber) { guitarId =>
      get {
        val guitarFuture = (guitarDB ? FindGuitarById(guitarId)).mapTo[Option[Guitar]].map(_.toJson.prettyPrint).map(toHttpEntity)
        complete(guitarFuture)
      }
    }


  val simplifiedHightLevelRoute =
    (pathPrefix("api" / "guitar") & get) {
      (path(IntNumber) | parameter("id".as[Int])) { id =>
        val guitarFuture = (guitarDB ? FindGuitarById(id)).mapTo[Option[Guitar]]
        val entityFuture = guitarFuture.map { guitarOpt =>
          HttpEntity(ContentTypes.`application/json`,
            guitarOpt.toJson.prettyPrint
          )
        }
        complete(entityFuture)
      } ~ {
        val guitarListFuture = (guitarDB ? FindAllGuitar).mapTo[List[Guitar]]
        val entityFuture = guitarListFuture.map { guitars =>
          HttpEntity(ContentTypes.`application/json`, guitars.toJson.prettyPrint)
        }
        complete(entityFuture)
      }
    }


  Http().bindAndHandle(simplifiedHightLevelRoute, "localhost", 8080)

}

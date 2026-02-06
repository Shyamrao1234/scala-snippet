package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.stream.ActorMaterializer
import spray.json._

import scala.util.{Failure, Success}

trait PersonDefaultProtocol extends DefaultJsonProtocol {
  implicit val jsonFormat = jsonFormat2(HigherLevelExercise_2.Person)
}


object HigherLevelExercise_2 extends App with PersonDefaultProtocol {

  implicit val sytem = ActorSystem("HighLevelExercise_V2")
  implicit val materializer = ActorMaterializer()

  import sytem.dispatcher
  import akka.http.scaladsl.server.Directives._

  /** *
   * Exercise :
   * -GET /api/people :retrieve ALL the people you have registered
   * -GET /api/people/pin : retrieve the person with that PIN, return as JSON
   * -GET /api/people?pin=x (same)
   * -POST /api/people  with a JSON payload denoting a Person, add that to your database
   */

  case class Person(pin: Int, name: String)

  var people = List(
    Person(1, "Alice"),
    Person(2, "Bob"),
    Person(3, "Charlie")
  )


  val personRouteServer =
    pathPrefix("api" / "people") {
      get {
        (path(IntNumber) | parameter("pin".as[Int])) { pin =>
          val findPerson = people.find(_.pin == pin)
          complete(HttpEntity(ContentTypes.`application/json`, findPerson.toJson.prettyPrint))
        } ~ pathEndOrSingleSlash {
          complete(HttpEntity(ContentTypes.`application/json`, people.toJson.prettyPrint))
        }
      } ~ (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) =>
        val payloadFuture = request.entity.dataBytes.map(_.utf8String).runFold("")(_ ++ _)
        val personEntity = payloadFuture.map(each => each.parseJson.convertTo[Person])
        onComplete(personEntity) {
          case Success(person) =>
            log.info(s"Got person: ${person}")
            people = people :+ person
            complete(StatusCodes.OK)
          case Failure(exception) =>
            log.warning(s"Something failed with the fetching the person from the entity ${exception}")
            failWith(exception)
        }
        //        personEntity.onComplete {
        //          case Success(person) =>
        //            log.info(s"Got person: ${person}")
        //            people = people :+ person
        //          case Failure(exception) =>
        //            log.warning(s"Something failed with the fetching the person from the entity ${exception}")
        //        }
        //        complete(personEntity.map(_ => StatusCodes.OK)
        //          .recover {
        //            case _ => StatusCodes.InternalServerError
        //          })
      }
    }


  Http().bindAndHandle(personRouteServer, "localhost", 8080)


}


object Person {

  val json =
    """
      |{
      |  "name": "Alice",
      |  "pin": 1
      |}
      |""".stripMargin
}
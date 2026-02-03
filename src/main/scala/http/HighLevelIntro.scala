package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.stream.ActorMaterializer

object HighLevelIntro extends App {

  implicit val system = ActorSystem("highlevelIntro")
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  val simpleRoute =
    path("home") {
      complete(StatusCodes.OK)
    }

  val pathGetRoute =
    path("home") {
      get {
        complete(StatusCodes.OK)
      }
    }


  val chainedRoute =
    path("myEndPoint") {
      get {
        complete(StatusCodes.OK)
      } ~ post {
        complete(StatusCodes.Forbidden)
      }
    } ~ path("home"){
      get{
        complete(
          HttpEntity(ContentTypes.`text/plain(UTF-8)`,"Hello akka HTTP")
        )
      }
    }


  Http().bindAndHandle(chainedRoute, "localhost", 8080)

}

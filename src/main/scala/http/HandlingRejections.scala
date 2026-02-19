package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MissingQueryParamRejection, RejectionHandler, ValidationRejection}

object HandlingRejections extends App {

  implicit val system = ActorSystem("HandlingRejections")
  implicit val materializer = ActorMaterializer

  import system.dispatcher

  val badRequestHandler: RejectionHandler = { rejections =>
    println(s"I have encountered rejections ${rejections}")
    Some(complete(StatusCodes.BadRequest))
  }

  val forbiddenHandler: RejectionHandler = { rejections =>
    println(s"I have encountered rejections ${rejections}")
    Some(complete(StatusCodes.Forbidden))
  }

  val simpleRouteWithHandler =
    handleRejections(badRequestHandler) {
      get {
        complete(StatusCodes.OK)
      } ~ post {
        handleRejections(forbiddenHandler) {
          parameter("myParam") { _ =>
            complete(StatusCodes.OK)
          }
        }
      }
    }

  Http().bindAndHandle(simpleRouteWithHandler, "localhost", 8080)

}

object HandleRejection_V2 extends App {

  implicit val system = ActorSystem("handleRejection_V2")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val customRejections = RejectionHandler.newBuilder()
    .handle {
      case MissingQueryParamRejection(parameterName) =>
        complete(StatusCodes.BadRequest, s"Parameter ${parameterName} is required")
    }.handle {
      case ValidationRejection(msg, _) =>
        complete(StatusCodes.BadRequest, s"Validation failed: ${msg}")
    }.result()

  val route =
    handleRejections(customRejections) {
      path("test") {
        parameter("id") { _ =>
          complete("Ok")
        }
      }
    }


}
package http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer

object HandlingExceptions extends App {

  implicit val system = ActorSystem("HandlingException")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  import akka.http.scaladsl.server
  import akka.http.scaladsl.server.Directives._

  val simpleRoute =
    path("api" / "people") {
      get {
        throw new RuntimeException("Getting all the people took to long")
      } ~ post {
        parameter("id") { id =>
          throw new RuntimeException(s"Parameter ${id} cannot be found in the dababase, TABLE FLIP")

          complete(StatusCodes.OK)
        }
      }
    }

  implicit val customExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: RuntimeException =>
      complete(StatusCodes.NotFound, ex.getMessage)
    case ex: IllegalArgumentException =>
      complete(StatusCodes.BadRequest, ex.getMessage)
  }

  import akka.http.scaladsl.Http

  //  Http().bindAndHandle(simpleRoute, "localhost", 8080)

  val handleRuntimeException: ExceptionHandler = ExceptionHandler {
    case ex: RuntimeException =>
      complete(StatusCodes.NotFound, ex.getMessage)
  }

  val handleIllegalArgument: ExceptionHandler = ExceptionHandler {
    case ex: IllegalArgumentException =>
      complete(StatusCodes.BadRequest, ex.getMessage)
  }

  val simpleWithExceptionHandler =
    handleExceptions(handleRuntimeException) {
      path("api" / "people") {
        get {
          throw new RuntimeException("Getting all the people took to long")
        } ~ handleExceptions(handleIllegalArgument) {
          post {
            parameter("id") { id =>
              throw new RuntimeException(s"Parameter ${id} cannot be found in the dababase, TABLE FLIP")

              complete(StatusCodes.OK)
            }
          }
        }
      }
    }

  Http().bindAndHandle(simpleWithExceptionHandler, "localhost", 8080)


}


/**
 * akka http has default exception handler with 500 sever not found
 */

object HandleException_V2 extends App {

  implicit val system = ActorSystem("HandleException_V2")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher
  import akka.http.scaladsl.server.Directives._

  val customExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: RuntimeException =>
      throw new ArithmeticException("Exception thrown inside exception handler method")
//      complete(StatusCodes.BadRequest)
  }

  val simpleServer = handleExceptions(customExceptionHandler) {
    path("api" / "people") {
      get {
        throw new RuntimeException("Simply throwing exception to test")
        complete(StatusCodes.OK)
      }
    }
  }

  import akka.http.scaladsl.Http

  Http().bindAndHandle(simpleServer, "localhost", 8080)
}
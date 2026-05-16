package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object DirectiveBreakDown extends App {

  implicit val system       = ActorSystem("DirectivesBreakDown")
  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._

  /** *
   * Type #1 : filtering directives
   */
  val simpleHttpMethodRoute =
    post {
      complete(StatusCodes.Forbidden)
    }

  val simplePathRoute =
    path("about") {
      complete(
        HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello from the about page")
      )
    }

  val complexPaths =
    path("api" / "myEndPoint") {
      complete(StatusCodes.OK)
    }

  val confusedComplexPath = path("api/myEndPoint") {
    complete(StatusCodes.NotImplemented)
  }

  val pathEndRoute =
    pathEndOrSingleSlash { // localhost:8080 OR localhost:8080/
      complete(StatusCodes.OK)
    }

  /** *
   * Type #2 : extraction directives
   */

  //GET on /api/item/42
  val pathExtractionRoute =
    path("api" / "item" / IntNumber) { (itemNumber: Int) =>
      println(s"I've got a number in my path : ${itemNumber}")
      complete(StatusCodes.OK)
    }


  val pathMultiExtractionRoute = {
    path("api" / "item" / IntNumber / IntNumber) { (number1, number2) =>
      complete(StatusCodes.OK)
    }
  }

  /**
   * Extract Query parameter
   */

  // api/item?id=36
  val queryParamExtractRoute = {
    path("api" / "item") {
      parameter("id".as[Int]) { (itemId: Int) =>
        println(s"I've extracted the ID as ${itemId}")
        complete(StatusCodes.OK)
      }
    }
  }

  /** *
   * Extract Http Request
   */

  val extarctHttpRequest: Route =
    path("myEndPoint") {
      extractRequest { (request: HttpRequest) =>
        println("request ---" + request)
        complete(StatusCodes.OK)
      }
    }


  /**
   *
   * Type #3 : composite direction
   */
  val simpleNestedRoute =
    path("api" / "item") {
      get {
        complete(StatusCodes.OK)
      }
    }

  val compactSimpleNested = (path("api" / "item") & get) {
    complete(StatusCodes.OK)
  }

  val compactExtractRequst =
    (path("controlEndPoint") & extractRequest & extractLog) { (request, log) =>
      complete(StatusCodes.OK)
    }

  /**
   * using or
   */
  val dryRoute = {
    (path("about") | path("aboutUs")) {
      complete(StatusCodes.OK)
    }
  }


  val blogIdRoute = {
    (path(IntNumber) | parameter("postId".as[Int])) { (id: Int) =>
      complete(StatusCodes.OK)
    }
  }

  val failedRoute =
    path("notSupported") {
      failWith(new RuntimeException("Unsupported"))
    }

  val routeWithRejection =
    path("home") {
      reject
    }


  Http().bindAndHandle(extarctHttpRequest, "localhost", 8080)

}

package http


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.IncomingConnection
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse, StatusCodes, Uri}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.util.{Failure, Success}

object LowLevelAPI extends App {

  implicit val system = ActorSystem("LowLevelAPI")
  implicit val materializer = ActorMaterializer()

  import system.dispatcher


  val serverBinding = Http().bind("localhost", 8080)
  val sink = Sink.foreach[IncomingConnection] { incomming =>
    println(s"Incoming connection ${incomming.remoteAddress}")
  }

  //  val graph = serverBinding.to(sink).run
  //  graph.onComplete {
  //    case Failure(exception) => println("connection failed")
  //    case Success(value) => println("Successfully accessible")
  //  }


  /** *
   * Synchronized
   */
  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.GET, Uri.Path("/home"), _, entity, _) =>
      HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello Akka"))
    case _ => HttpResponse(StatusCodes.NotFound)
  }

  val httpFuture = Http().bindAndHandleSync(requestHandler, "localhost") // if you don't specify the port it will read 80

  httpFuture.onComplete {
    case Failure(exception) => println(s"failed with ${exception}")
    case Success(value) => println(s"Successfully connected ${value.localAddress.getPort}")
  }

  /**
   * Exercise : create your own http server running on localhost 8388, which replies
   *  - with a welcome message on the "front door" localhost"8388
   *  - with a proper HTML on localhost:8388/about
   *  - with a 404 message otherwise
   */

  val requestHandler_4: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(
        StatusCodes.OK,
        entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, "Hello from front door")
      )
    case HttpRequest(HttpMethods.GET, Uri.Path("/about"), _, _, _) =>
      HttpResponse(
        StatusCodes.OK,
        entity = HttpEntity(ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            |<body>
            |<div style= "color: red">
            |Hello I am bout page
            |<div>
            |</body>
            |</html>
            |""".stripMargin)
      )

    case HttpRequest(HttpMethods.GET,Uri.Path("/search"),_,_,_) =>
      HttpResponse(
        StatusCodes.Found,
        headers = List(Location("http://google.com"))
      )
    case request: HttpRequest=>
      request.discardEntityBytes()
      HttpResponse(
        StatusCodes.NotFound,
        entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "OOps not found")
      )
  }

  Http().bindAndHandleSync(requestHandler_4, "localhost", 8388)


}

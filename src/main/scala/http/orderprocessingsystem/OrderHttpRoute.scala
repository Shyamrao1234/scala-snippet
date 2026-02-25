package http.orderprocessingsystem

/**
 * Created by Shyamrao on Feb 25, 2026.
 */

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object OrderHttpRoute extends App{

  implicit val actorSystem  = ActorSystem("OrderHttpRoute")
  implicit val materializer = ActorMaterializer

  val apiRoute = pathPrefix("order") {
      post {
        entity(as[String]) { body =>
          println(s"Received order : ${body}")
          complete("Order placed successfully")
        }
      } ~ path(Segment) { id =>
      get {
        complete(s"📦 Order $id is being processed")
      }
    }
  }

  val uiRoute =
    pathSingleSlash {
      getFromResource("public/index.html")
    } ~ getFromResourceDirectory("public")

  val routes = apiRoute ~ uiRoute

  Http().bindAndHandle(routes,"localhost",8080)

}

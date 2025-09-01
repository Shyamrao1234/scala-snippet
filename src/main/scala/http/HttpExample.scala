package http

import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server._

object HttpExample extends App {

  val route:Route = { ctx => ctx.complete("yeah") }

  val route1:Route=_.complete("yeah")

  val route2:Route = complete("yeah")
}

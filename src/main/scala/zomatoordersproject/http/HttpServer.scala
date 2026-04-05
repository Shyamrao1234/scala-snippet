package zomatoordersproject.http

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

import akka.actor.typed._
import akka.http.scaladsl.Http
import akka.actor.typed.scaladsl.adapter._
import scala.concurrent.ExecutionContext

object HttpServer {

  def start(system: ActorSystem[_], routes: OrderRoutes): Unit = {

    implicit val sys = system.toClassic
    implicit val ec: ExecutionContext = system.executionContext

    Http().newServerAt("localhost", 8080).bind(routes.routes)
  }

}

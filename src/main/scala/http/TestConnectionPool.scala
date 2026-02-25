package http

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ConnectionPoolSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer

/**
 * Created by Shyamrao on Feb 20, 2026.
 */

import  scala.concurrent.ExecutionContext.Implicits.global
object TestConnectionPool extends App {

  implicit val actorSystem  = ActorSystem("testConnectionPool")
  implicit val materializer = ActorMaterializer

  val url = "https://httpbin.org/stream-bytes/10000000"

  val connectionPoolSetting=ConnectionPoolSettings(actorSystem).withMaxConnections(2)

  for {
    r1 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r1  " + r1)
    r2 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r2 " + r2)
    r3 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r3" + r3)
    r4 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r4 " + r4)
    r5 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r5 " + r5)
    r6 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r6 " + r6)
    r7 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r7 " + r7)
    r8 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r8 " + r8)
    r9 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r9 " + r9)
    r10 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r10 " + r10)
    r11 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r11 " + r11)
    r12 <- Http().singleRequest(HttpRequest(uri = url),settings = connectionPoolSetting).map(_.discardEntityBytes())
    _ = println("Received request r12 " + r12)

  } yield ()

}

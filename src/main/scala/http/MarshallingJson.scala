package http

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.stream.ActorMaterializer
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

case class Player(nickName: String, characterClass: String, level: Int)
//step - 1

import spray.json._


object GameAreaMap {
  case object GetAllPlayers

  case class GetPlayer(nickname: String)

  case class GetPlayerByClass(characterClass: String)

  case class AddPlayer(player: Player)

  case class RemovePlayer(player: Player)

  case object OperationSuccess
}

class GameAreaMap extends Actor with ActorLogging {

  import GameAreaMap._

  var players = Map[String, Player]()

  override def receive: Receive = {
    case GetAllPlayers =>
      log.info("Getting all players")
      sender() ! players.values.toList

    case GetPlayer(nickName) =>
      log.info(s"Getting player with nickname ${nickName}")
      sender() ! players.get(nickName)
    case GetPlayerByClass(characterClass: String) =>
      log.info(s"Getting player with characterClass ${characterClass}")
      sender() ! players.values.toList.filter(_.characterClass == characterClass)
    case AddPlayer(player) =>
      log.info(s"Trying to add player ${player}")
      players = players + (player.nickName -> player)
      sender() ! OperationSuccess
    case RemovePlayer(player) =>
      log.info(s"Trying to remove ${player}")
      players = players - (player.nickName)
      sender() ! OperationSuccess
  }
}

trait PlayerJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat3(Player)
}

object MarshallingJson extends App with PlayerJsonProtocol with SprayJsonSupport {

  implicit val system = ActorSystem("MarshallingJson")
  implicit val materialize = ActorMaterializer

  import system.dispatcher

  import GameAreaMap._

  val rtjvmGameMap = system.actorOf(Props[GameAreaMap], "rockTheJVMGameAreaMap")
  val playersList = List(
    Player("martin_killx u", "warrior", 70),
    Player("ronaldbraveheart007", "Elf", 67),
    Player("daniel_rock03", "wizard", 30)
  )

  playersList.foreach { player =>
    rtjvmGameMap ! AddPlayer(player)
  }

  /** *
   * -GET /api/player, return all the players in the map, as JSON
   * -GET /api/player/nickName, return the player with the given nickname (as json)
   * -GET /api/player?nickName=x, does the same
   * -GET /api/player/class/charClass, return all the players with the given character class
   * -GET /api/player with JSON payload, adds the player to the map
   * (Exercise) DELETE /api/player with JSON payload, removes the player from the map
   */

  import akka.http.scaladsl.server.Directives._

  implicit val timeout = Timeout(2.seconds)

  val playerServer =
    pathPrefix("api" / "player") {
      get {
        path("class" / Segment) { characterClass =>
          val playerByClassFuture = (rtjvmGameMap ? GetPlayerByClass(characterClass)).mapTo[List[Player]]
          complete(playerByClassFuture)
        } ~ (path(Segment) | parameter("nickname")) { nickName =>
          val playerOptFuture = (rtjvmGameMap ? GetPlayer(nickName)).mapTo[Option[Player]]
          complete(playerOptFuture)
        } ~ pathEndOrSingleSlash {
          val allPlayersFuture = (rtjvmGameMap ? GetAllPlayers).mapTo[List[Player]]
          complete(allPlayersFuture)
        }
      } ~ post {
        entity(as[Player]) { player =>
          complete((rtjvmGameMap ? AddPlayer(player)).map(_ => StatusCodes.OK))
        }
      } ~ delete {
        entity(as[Player]) { player =>
          complete((rtjvmGameMap ? RemovePlayer(player)).map(_ => StatusCodes.OK))
        }
      }
    }

  Http().bindAndHandle(playerServer, "localhost", 8080)

}

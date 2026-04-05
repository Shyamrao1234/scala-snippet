package akkatypedactors

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

object AkkaConfigurationDemo {

  object SimpleLoggingActor {
    def apply(): Behavior[String] = Behaviors.receive { (context, message) =>
      context.log.debug(message)
      context.log.info(message)
      context.log.warn(message)
      context.log.error(message)
      Behaviors.same
    }
  }


  def inlineConfig() = {
    val configString =
      """
        |akka{
        | loglevel = "INFO"
        |}
        |""".stripMargin

    val config      = ConfigFactory.parseString(configString)
    val actorSystem = ActorSystem(SimpleLoggingActor(),"ConfigDemo",ConfigFactory.load(config))

    actorSystem ! "Hello I am shyam"
    actorSystem.terminate()
  }


  def configFile() = {
    val config=ConfigFactory.load().getConfig("MySpecificConfig")
    val actorSystem = ActorSystem(SimpleLoggingActor(),"ConfigDemo",ConfigFactory.load(config))

    actorSystem ! "Hello I am shyam"
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    inlineConfig()
  }

}

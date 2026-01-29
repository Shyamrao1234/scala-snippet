package akkatypedactors

import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior, SupervisorStrategy}

import scala.util.Random

object RouterPoolExample {

  trait Command

  case class DoLog(text: String) extends Command

  object WorkerActor {
    def apply(counter: ActorRef[IncrementCommand]): Behavior[Command] = active(counter)


    def active(counter: ActorRef[IncrementCommand]): Behavior[Command] =
      Behaviors.receive { (context, message) =>
        message match {
          case DoLog(text) =>
            if (Random.nextInt(10) == 2 || Random.nextInt(10) == 4 || Random.nextInt(10) == 8) throw new RuntimeException("Failed due >>>>>>>")
            counter ! Increment(text, context.self)
            Behaviors.same
        }
      }
  }

  trait IncrementCommand

  case class Increment(text: String, worker: ActorRef[Command]) extends IncrementCommand

  object IncrementActor {
    def apply(): Behavior[IncrementCommand] = active()

    def active(count: Int = 0): Behavior[IncrementCommand] = Behaviors.receive { (context, message) =>
      message match {
        case Increment(text, worker) =>
          context.log.info(s"[ProcessTask - ${worker.path.name}] Received text ${text} - ${count}")
          active(count + 1)
      }
    }
  }

  def testPoolRouter() = {

    val userGuardian = Behaviors.setup[Unit] { context =>
      val incrementActor = context.spawn(IncrementActor(), "increment")
      val pool = Routers.pool(4) {
        Behaviors.supervise(WorkerActor(incrementActor)).onFailure[Exception](SupervisorStrategy.restart)
      }

      val router = context.spawn(pool, "worker-pool")

      (1 to 10).foreach { n =>
        router ! DoLog("Hello Log")
      }

      Behaviors.empty
    }
    val actorSystem = ActorSystem(userGuardian, "user-guardian")
    Thread.sleep(1000)
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    testPoolRouter()
  }


}


object RouterExample_2 {


  object Worker {
    sealed trait Command

    case class DoWork(taskId: Int) extends Command

    def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
        case DoWork(taskId) =>
          context.log.info(s"[${taskId}] - ${context.self.path.name} doing work")
          Behaviors.same
      }
    }
  }

  object Router {
    def apply(): Behavior[Worker.Command] = Behaviors.setup { context =>
      val router = Routers.pool(4) {
        Worker()
      }
      router
    }
  }


  def testRouter(): Unit = {
    val userGuardian: Behavior[Unit] = Behaviors.setup { context =>
      val routerActor = context.spawn(Router(), "router")
      (1 to 10000).foreach { taskId =>
        routerActor ! Worker.DoWork(taskId)
      }
      Behaviors.empty
    }

    val actorSystem = ActorSystem(userGuardian, "userGuardian")
    Thread.sleep(10000)
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    testRouter()
  }

}


object RouterExample {

  object Worker extends App {
    trait Command

    case class DoWork(taskId: Int) extends Command

    def apply(): Behavior[Command] = Behaviors.receive { (context, message) =>
      message match {
        case DoWork(taskId) =>
          if(taskId == 2 || taskId==4 ){
            throw new RuntimeException("Failed due to custom issue")
          }else println(s"Task Id - ${taskId}, Actor : ${context.self.path.name}")
          Behaviors.same
        case _ => Behaviors.same
      }
    }

  }


  object RouterActor {
    def apply(): Behavior[Worker.Command] = Behaviors.setup { context =>
      val router = Routers.pool(4) {
        Worker()
      }
      router
    }
  }


  def test() = {
    val userGuardian: Behavior[Unit] = Behaviors.setup { context =>
      val routerActor = context.spawn(RouterActor(), "router")
      for (i <- 1 to 100) {
        routerActor ! Worker.DoWork(i)
      }
      Behaviors.empty
    }
    val supervice = Behaviors.supervise(userGuardian).onFailure[Exception](SupervisorStrategy.restart)

    val actorSystem = ActorSystem(supervice, "user")
    Thread.sleep(1000)
    actorSystem.terminate()
  }

  def main(args: Array[String]): Unit = {
    test()
  }


}
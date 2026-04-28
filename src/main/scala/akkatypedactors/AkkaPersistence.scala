package akkatypedactors

import akka.persistence.typed.scaladsl.EventSourcedBehavior

/**
 * Created by LENOVO on Apr 21, 2026.
 */

object AkkaPersistence extends App {

  trait Command

  case class Deposit(amount: Int) extends Command

  case class WithDraw(amount: Int) extends Command


  trait Event

  case class Deposited(amount: Int) extends Event

  case class WithDrawn(amount: Int) extends Event

  case class State(balance: Int = 0)

  def apply(accountNumber: String): EventSourcedBehavior[Command, Event, State] = {
   EventSourcedBehavior(
     persistenceId = ???,
     emptyState = ???,
     commandHandler = ???,
     eventHandler = ???
   )
  }

}

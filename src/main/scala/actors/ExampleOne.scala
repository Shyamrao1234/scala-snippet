package actors

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem._
import com.typesafe.config.ConfigFactory

//object ExampleOne extends App {
//
//  class SimpleActor extends Actor {
//    override def receive: Receive = {
//      case s: String => println(s"string ${s}")
//      case i: Int    => println(s"int ${i}")
//    }
//  }
//
//  val system = ActorSystem("SimpleSystem")
//  val actor  = system.actorOf(Props[SimpleActor])
//
//  actor ! "hii this is shyam"
//  actor ! 25
//
//  system.terminate()
//}


import akka.actor.typed.{ActorRef, Behavior}
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}

object ShoppingCart {

  // --- 1. State ---
  final case class State(items: Map[String, Int], isCheckedOut: Boolean) {
    def updateItem(itemId: String, quantity: Int): State = {
      val newQuantity = items.getOrElse(itemId, 0) + quantity
      copy(items = items + (itemId -> newQuantity))
    }

    def checkout: State = copy(isCheckedOut = true)
  }


  object State {
    val empty: State = State(Map.empty, isCheckedOut = false)
  }

  // --- 2. Commands ---
  sealed trait Command

  final case class AddItem(itemId: String, quantity: Int, replyTo: ActorRef[Response]) extends Command

  final case class Checkout(replyTo: ActorRef[Response]) extends Command

  sealed trait Response

  final case class Accepted(summary: String) extends Response

  final case class Rejected(reason: String) extends Response

  // --- 3. Events ---
  sealed trait Event

  final case class ItemAdded(itemId: String, quantity: Int) extends Event

  case object CartCheckedOut extends Event

  // --- 4. The Behavior ---
  def apply(cartId: String): Behavior[Command] = {
    EventSourcedBehavior[Command, Event, State](
      persistenceId = PersistenceId.ofUniqueId(cartId),
      emptyState = State.empty,
      commandHandler = (state, command) => handleCommand(cartId, state, command),
      eventHandler = (state, event) => handleEvent(state, event)
    )
  }

  // --- 5. Command Handler (Validation & Side Effects) ---
  private def handleCommand(cartId: String, state: State, command: Command): Effect[Event, State] = {
    if (state.isCheckedOut) {
      command match {
        case AddItem(_, _, replyTo) =>
          Effect.none.thenRun(_ => replyTo ! Rejected("Cart is already checked out"))
        case Checkout(replyTo)      =>
          Effect.none.thenRun(_ => replyTo ! Rejected("Already checked out"))
      }
    } else {
      command match {
        case AddItem(itemId, quantity, replyTo) =>
          if (quantity <= 0) {
            Effect.none.thenRun(_ => replyTo ! Rejected("Quantity must be greater than zero"))
          } else {
            // Valid command: Persist the event
            Effect
              .persist(ItemAdded(itemId, quantity))
              .thenRun(_ => replyTo ! Accepted(s"Added $quantity of $itemId"))
          }

        case Checkout(replyTo) =>
          if (state.items.isEmpty) {
            Effect.none.thenRun(_ => replyTo ! Rejected("Cannot checkout an empty cart"))
          } else {
            Effect
              .persist(CartCheckedOut)
              .thenRun(_ => replyTo ! Accepted("Checkout successful"))
          }
      }
    }
  }

  // --- 6. Event Handler (Strict State Mutation) ---
  // CRITICAL: This must be a pure function. No side effects, no sending messages.
  private def handleEvent(state: State, event: Event): State = {
    event match {
      case ItemAdded(itemId, quantity) => state.updateItem(itemId, quantity)
      case CartCheckedOut              => state.checkout
    }
  }


}

object RunShoppingCard extends App {

  val rootBehaviour = Behaviors.setup[Unit] { context =>

    val cart = context.spawn(ShoppingCart("card-123"), "myshoppingcard")

    val printer = context.spawn(Behaviors.receive[ShoppingCart.Response] { (context, message) =>
      message match {
        case ShoppingCart.Accepted(summary) => context.log.info(s"✅ SUCCESS: $summary"); Behaviors.same
        case ShoppingCart.Rejected(reason)  => context.log.warn(s"❌ REJECTED: $reason"); Behaviors.same
      }
    }, "reply-printer")

    // Send some commands to the cart!
    cart ! ShoppingCart.AddItem("apple", 2, printer)
    cart ! ShoppingCart.AddItem("banana", 5, printer)
    cart ! ShoppingCart.Checkout(printer)

    // This one should fail because the cart is already checked out
    cart ! ShoppingCart.AddItem("orange", 1, printer)

    Behaviors.empty
  }

  // 2. In-Memory Configuration (So it doesn't crash looking for a real database)
  val config = ConfigFactory.parseString(
    """
      akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
      akka.persistence.snapshot-store.plugin = "akka.persistence.snapshot-store.local"
      akka.persistence.snapshot-store.local.dir = "target/snapshots"
    """
  )

  // 3. Start the Actor System
  val system = ActorSystem(rootBehaviour, "ShoppingSystem", config)


}
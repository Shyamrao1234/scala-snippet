package zomatoordersproject.actors

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.persistence.testkit.scaladsl.EventSourcedBehaviorTestKit
import com.typesafe.config.ConfigFactory
import org.scalatest.wordspec.AnyWordSpecLike
import zomatoordersproject.domain._

class OrderActorSpec extends ScalaTestWithActorTestKit(
  EventSourcedBehaviorTestKit.config.withFallback(ConfigFactory.parseString(
    """
      akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
      akka.persistence.snapshot-store.plugin = "akka.persistence.snapshot-store.local"
      akka.persistence.snapshot-store.local.dir = "target/snapshots-test"
    """))) with AnyWordSpecLike {

  "OrderActor" should {
    "persist OrderCreated event and update state" in {
      val orderId = "test-order-1"
      val testKit = EventSourcedBehaviorTestKit[OrderActor.Command, OrderActor.Event, Option[Order]](
        system,
        OrderActor(orderId)
      )

      val order = Order.create(orderId, "user-1", List("pizza"))
      val result = testKit.runCommand[Order](replyTo => OrderActor.CreateOrder(order, replyTo))

      result.reply shouldBe order
      result.event shouldBe OrderActor.OrderCreated(order)
      result.stateOfType[Some[Order]].value shouldBe order
    }

    "persist StatusUpdated event" in {
      val orderId = "test-order-2"
      val testKit = EventSourcedBehaviorTestKit[OrderActor.Command, OrderActor.Event, Option[Order]](
        system,
        OrderActor(orderId)
      )

      val order = Order.create(orderId, "user-2", List("burger"))
      testKit.runCommand[Order](replyTo => OrderActor.CreateOrder(order, replyTo))

      val updateResult = testKit.runCommand[Order](replyTo => OrderActor.UpdateStatus(OrderStatus.Confirmed, replyTo))
      
      updateResult.reply.status shouldBe OrderStatus.Confirmed
      updateResult.event shouldBe OrderActor.StatusUpdated(OrderStatus.Confirmed)
    }
  }
}

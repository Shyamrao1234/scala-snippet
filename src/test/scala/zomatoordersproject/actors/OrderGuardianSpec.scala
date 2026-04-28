package zomatoordersproject.actors

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import com.typesafe.config.ConfigFactory
import org.scalatest.wordspec.AnyWordSpecLike
import zomatoordersproject.domain._

class OrderGuardianSpec extends ScalaTestWithActorTestKit(ConfigFactory.parseString(
  """
    akka.persistence.journal.plugin = "akka.persistence.journal.inmem"
    akka.persistence.snapshot-store.plugin = "akka.persistence.snapshot-store.local"
    akka.persistence.snapshot-store.local.dir = "target/snapshots-test-guardian"
  """
)) with AnyWordSpecLike {

  "OrderGuardian" should {
    "spawn child actors and forward messages" in {
      val guardian = testKit.spawn(OrderGuardian())
      val probe = testKit.createTestProbe[Order]()

      val order = Order.create("guardian-test-1", "user-1", List("fries"))
      guardian ! OrderGuardian.CreateOrder(order, probe.ref)
      
      probe.expectMessage(order)

      val probe2 = testKit.createTestProbe[Option[Order]]()
      guardian ! OrderGuardian.GetOrder("guardian-test-1", probe2.ref)
      probe2.expectMessage(Some(order))
    }
  }
}

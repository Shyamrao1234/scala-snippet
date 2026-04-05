package akkatypedactors

/**
 * Created by Shyamrao on Mar 20, 2026.
 */

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.wordspec.AnyWordSpecLike

class EchoTest extends ScalaTestWithActorTestKit
  with AnyWordSpecLike {

  "Actor Test" should {
    "Test Echo file" in {
      val pinger = testKit.spawn(Echo(), "TestEcho")
      val probe  = testKit.createTestProbe[Echo.Pong]()
      pinger ! Echo.Ping("Hello", probe.ref)
      probe.expectMessage(Echo.Pong("Hell"))
    }
  }

}

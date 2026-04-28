package zomatoordersproject.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ZomatoLoadSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val createOrderPayload =
    """{
      |  "orderId": "order-${randomId}",
      |  "userId": "user-123",
      |  "items": ["pizza", "coke"]
      |}""".stripMargin

  val scn = scenario("Zomato Orders Load Test")
    .exec(session => session.set("randomId", java.util.UUID.randomUUID().toString))
    .exec(
      http("Create Order")
        .post("/orders")
        .body(StringBody(createOrderPayload))
        .check(status.is(201))
    )
    .pause(1)
    .exec(
      http("Get Order")
        .get("/orders/order-${randomId}")
        .check(status.is(200))
    )
    .pause(1)
    .exec(
      http("Update Order")
        .put("/orders/update/order-${randomId}")
        .body(StringBody("""{"status": "PREPARING"}"""))
        .check(status.is(200))
    )

  setUp(
    scn.inject(
      rampUsersPerSec(10).to(100).during(10.seconds), // Ramp up to 100 RPS
      constantUsersPerSec(100).during(30.seconds)    // Hold at 100 RPS for 30s
    )
  ).protocols(httpProtocol)

}

package zomatoordersproject.domain

import java.time.Instant

/**
 * Enriched domain model.
 *
 * Changes vs original:
 *  - status is OrderStatus ADT, not raw String
 *  - createdAt / updatedAt for audit trail
 *  - items is NonEmptyList conceptually (validated at service layer)
 *
 * Kept as a case class for easy Akka message passing (immutable).
 */
case class Order(
    orderId:   String,
    userId:    String,
    items:     List[String],
    status:    OrderStatus,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now()
) {
  def withStatus(s: OrderStatus): Order =
    copy(status = s, updatedAt = Instant.now())
}

object Order {
  /** Convenience constructor — new orders always start as Pending */
  def create(orderId: String, userId: String, items: List[String]): Order =
    Order(orderId, userId, items, OrderStatus.Pending)
}

// ── API request/response DTOs (separate from domain) ──────────────────────────

/** What the client POSTs to create an order */
case class CreateOrderRequest(
    orderId: String,
    userId:  String,
    items:   List[String]
)

/** What the client PATCHes to update status */
case class UpdateStatusRequest(status: String)

/** Standardized API error response */
case class ApiError(code: String, message: String)

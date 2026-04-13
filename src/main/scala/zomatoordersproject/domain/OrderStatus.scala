package zomatoordersproject.domain


/**
 * Sealed ADT for order status — prevents raw string misuse.
 * Replaces the unvalidated `status: String` field.
 */
sealed trait OrderStatus {
  def label: String
}

object OrderStatus {
  case object Pending    extends OrderStatus { val label = "PENDING"    }
  case object Confirmed  extends OrderStatus { val label = "CONFIRMED"  }
  case object Preparing  extends OrderStatus { val label = "PREPARING"  }
  case object OutForDelivery extends OrderStatus { val label = "OUT_FOR_DELIVERY" }
  case object Delivered  extends OrderStatus { val label = "DELIVERED"  }
  case object Cancelled  extends OrderStatus { val label = "CANCELLED"  }

  private val all: Map[String, OrderStatus] =
    Seq(Pending, Confirmed, Preparing, OutForDelivery, Delivered, Cancelled)
      .map(s => s.label -> s)
      .toMap

  def fromString(s: String): Either[String, OrderStatus] =
    all.get(s.toUpperCase).toRight(s"Invalid status: $s. Valid: ${all.keys.mkString(", ")}")
}

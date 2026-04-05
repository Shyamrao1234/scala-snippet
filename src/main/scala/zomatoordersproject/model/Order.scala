package zomatoordersproject.model

/**
 * Created by Shyamrao on Mar 07, 2026.
 */

case class Order(orderId: String,
                 items: List[String],
                 userId: String,
                 status: String)
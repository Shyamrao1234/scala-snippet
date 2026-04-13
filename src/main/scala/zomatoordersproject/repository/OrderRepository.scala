package zomatoordersproject.repository

import zomatoordersproject.domain.{Order, OrderStatus}

import scala.concurrent.Future

/**
 * Persistence abstraction — the service layer depends on this trait, not
 * on any concrete storage.  Swap InMemoryOrderRepository for a Slick/Doobie
 * implementation without touching actors or routes.
 *
 * All methods return Future so the implementation can be non-blocking (DB, Redis, etc.).
 */
trait OrderRepository {
  def save(order: Order): Future[Order]
  def findById(id: String): Future[Option[Order]]
  def findAll(): Future[List[Order]]
  def updateStatus(id: String, status: OrderStatus): Future[Option[Order]]
  def delete(id: String): Future[Boolean]
}

// ── In-memory implementation (dev / testing only) ─────────────────────────────

import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._

/**
 * Thread-safe in-memory store backed by a ConcurrentHashMap.
 *
 * DESIGN NOTE — why not a mutable Map inside an actor?
 *   Putting storage inside an actor couples persistence to concurrency model.
 *   A repository trait lets you swap backends (Postgres, Redis) independently.
 *   For ~100 RPS the actor-as-state approach works, but breaks under shard
 *   rebalancing or actor restarts. A proper repository survives both.
 *
 * For production: replace with SlickOrderRepository backed by a connection pool
 *   (HikariCP, pool size = ~(cores * 2) + disk_spindles).
 */
class InMemoryOrderRepository(implicit ec: ExecutionContext) extends OrderRepository {

  private val store = new ConcurrentHashMap[String, Order]()

  override def save(order: Order): Future[Order] = Future {
    store.put(order.orderId, order)
    order
  }

  override def findById(id: String): Future[Option[Order]] = Future {
    Option(store.get(id))
  }

  override def findAll(): Future[List[Order]] = Future {
    store.values().asScala.toList
  }

  override def updateStatus(id: String, status: OrderStatus): Future[Option[Order]] = Future {
    Option(store.get(id)).map { existing =>
      val updated = existing.withStatus(status)
      store.put(id, updated)
      updated
    }
  }

  override def delete(id: String): Future[Boolean] = Future {
    store.remove(id) != null
  }
}

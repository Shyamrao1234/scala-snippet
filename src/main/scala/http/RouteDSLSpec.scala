package http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.{MethodRejection, Route}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec, stats}
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

case class Book(id: Int, author: String, title: String)

trait BookJsonProtocol extends DefaultJsonProtocol {
  implicit val bookJsonFormat = jsonFormat3(Book)
}

class RouteDSLSpec extends WordSpec with Matchers with ScalatestRouteTest with BookJsonProtocol {

  import RouteDSLSpec._

  "A digital library backend" should {

    "return all the books in the library" in {
      Get("/api/book") ~> library ~> check {
        status shouldBe StatusCodes.OK
        entityAs[List[Book]] shouldBe books
      }
    }


    "return a book for given query parameter" in {
      Get("/api/book?id=1") ~> library ~> check {
        status shouldBe StatusCodes.OK
        responseAs[Option[Book]] shouldBe Some(Book(1, "Harper Lee", "To Kill a Mockingbird"))
      }
    }

    "return a book by calling endpoint with the id in the path" in {
      Get("/api/book/1") ~> library ~> check {
        response.status shouldBe StatusCodes.OK

        val strictEntity = response.entity.toStrict(2.seconds)
        val bookOpt = strictEntity.map { strict =>
          strict.data.utf8String.parseJson.convertTo[Option[Book]]
        }
        val book = Await.result(bookOpt, 2.seconds)
        book shouldBe Some(Book(1, "Harper Lee", "To Kill a Mockingbird"))
      }
    }


    "One more way manually extracting the entity" in {
      Get("/api/book/1") ~> library ~> check {
        response.status shouldBe StatusCodes.OK

        val bookFutureOpt = response.entity.dataBytes.runFold(ByteString.empty)(_ ++ _)
          .map(_.utf8String.parseJson.convertTo[Option[Book]])
        val bookOpt = Await.result(bookFutureOpt, 2.seconds)
        bookOpt shouldBe Some(Book(1, "Harper Lee", "To Kill a Mockingbird"))
      }
    }

    "insert a book to the database" in {
      val newBook = Book(5, "rich dad poor dad", "james hocking")
      Post("/api/book", newBook) ~> library ~> check {
        status shouldBe StatusCodes.OK
        assert(books.contains(newBook))
      }
    }

    "should not accept delete method" in {
      Delete("/api/book") ~> library ~> check {
        rejections should not be empty

        val methodRejections = rejections.collect {
          case rejection: MethodRejection => rejection
        }
        methodRejections.length shouldBe 2
      }
    }

    "filter the books by the author" in {
      Get("/api/book/author/Harper%20Lee") ~> library ~> check {
        println(status)
        status shouldBe StatusCodes.OK
        responseAs[List[Book]] shouldBe List(Book(1, "Harper Lee", "To Kill a Mockingbird"))
      }
    }


    "Test case using route.seal" in {
      Get("/api/Dummy") ~> library ~> check {
        println(status)
      }
    }

    "what status return when we reject" in {
      Get("/api/test") ~> Route.seal(testRoute) ~> check {
        println(status)
      }
    }


  }
}

object RouteDSLSpec extends BookJsonProtocol with SprayJsonSupport {


  var books = List(
    Book(1, "Harper Lee", "To Kill a Mockingbird"),
    Book(2, "JRR Tolkien", "The Lord of the rings"),
    Book(3, "GRR Marting", "A song of ice and fire"),
    Book(4, "Tony Robbins", "Awaken the giant within")
  )


  /** *
   * GET /api/book - return all the books in the library
   * GET /api/book/X - return a single book with id x
   * GET /api/book?id=x - return a single book with id x
   * POST /api/book - add a new book to the library
   * GET /api/book/author/X - filter all the books with the given author
   */

  import akka.http.scaladsl.server.Directives._

  val testRoute =
    path("api" / "test") {
      get {
        reject
      }
    }

  val library =
    pathPrefix("api" / "book") {
      (path("author" / Segment) & get) { author =>
        complete(books.filter(_.author == author))
      } ~
        get {
          (path(IntNumber) | parameter("id".as[Int])) { id =>
            complete(books.find(_.id == id))
          } ~ pathEndOrSingleSlash {
            complete(StatusCodes.OK, books)
          }
        } ~ post {
        entity(as[Book]) { book =>
          books = books :+ book
          complete(StatusCodes.OK)
        }
      }
    }

}

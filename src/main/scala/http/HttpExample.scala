package http

import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

object HttpExample extends App {

  val route:Route = { ctx => ctx.complete("yeah") }

  val route1:Route=_.complete("yeah")

  val route2:Route = complete("yeah")

  val a:Route={
    println("MARK")
    ctx=>ctx.complete("yeah")
  }

  val b:Route={ctx=>
    println("MARK")
    ctx.complete("yeah")
  }

  val route3:Route={ctx=>
    if(ctx.request.method==HttpMethods.GET){
      ctx.complete("recived get")
    }else ctx.complete("recived something else")
  }


  val route3Simplify :Route=
    concat(
      get {
        complete("yeah get recived")
      },
      complete("something else returned")
    )

  val route4 : Route = get{
    complete("yeah")
  }


  val highLevelRoute=
    path("order" / IntNumber){id=>
      concat(
        get{
          complete("id:"+id)
        },
        put{
          complete("id:"+id)
        }
      )
    }

  





}

package lectures.part4Implicits

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {
  class P2PRequest
  class P2PResponse
  class Serializer[T]
  trait Actor{
    def receive(statusCode:Int):Int
    def receive(request: P2PRequest):Int
    def receive(response: P2PResponse):Int
    def receive[T:Serializer](message:T):Int
    def receive[T:Serializer](message:T,statusCode:Int):Int
    def receive(futue:Future[P2PRequest]):Int
    //def receive(futue:Future[P2PResponse]):Int
  }
  /*
  * 1. Type erasure
  * 2. lifting doesnot work for all overload methods
  *   val receiveFV = reecive_
  * 3. code duplication
  * 4. type inferencec and default args
  *
  * */

  trait  MessageMagnet[Result]{
    def apply():Result
  }
  def receive[R](magnet:MessageMagnet[R]):R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int]{
    override def apply(): Int = {
      println("Handling P2pRequests")
      42
    }
  }
  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int]{
    override def apply(): Int = {
      println("Handling P2P Response")
      24
    }
  }
  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1. No more type erasure problems

  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = {
      2
    }
  }

    implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int]{
      override def apply(): Int = {
        3
      }

  }
    println( receive(Future(new P2PRequest)))
    println(receive(Future(new P2PResponse) ))
  // 2- lifting works

  class Handle{
    def handle(s: =>String){
      println(s)
      println(s)
    }
  }

  trait HandleMagnet{
    def apply():Unit
  }
  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s : => String) extends HandleMagnet{
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }
  def sideEffets():String = {
    println("Hellll")
    "hahah"
  }

  //handle(sideEffets())
  handle{
    println("Hello Scalal")
    "magnet"
  }


}

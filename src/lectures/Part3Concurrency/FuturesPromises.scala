package lectures.Part3Concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._
object FuturesPromises extends App {

  def calculateMeaningOfLife:Int = {
    Thread.sleep(2000)
    43
  }

  val future = Future{
    calculateMeaningOfLife
  } //(global) which is passed by compiler
  println(future.value) // Option[Try[Int]]
  println("Waiting for future")
  future.onComplete {
    case Success(value) => println(s"meaning of the life is $value")
    case Failure(exception) => println(s"I have failed with $exception")
  }

  Thread.sleep(3000)

  // mini social network

  case class Profile(id:String,name:String){

    def poke(anotherProfile:Profile) = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }
  object SocialNetwork {
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy",
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

   val random = new Random()

   // APi
   def fetchProfile(id:String):Future[Profile] = Future {
     Thread.sleep(random.nextInt(300))
     Profile(id,names(id))
   }
    def fetchBestFriend(profile:Profile):Future[Profile] = Future{
      Thread.sleep(400)
      val bfId= friends(profile.id)
      Profile(bfId,names(bfId))
    }

  }
  // client : Mark to poke bill
//  val mark =SocialNetwork.fetchProfile("fb.id.1-zuck")
//  mark.onComplete{
//    case Success(markProfile) =>{
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete{
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(exception) => exception.printStackTrace()
//      }
//
//    }
//    case Failure(exception) => exception.printStackTrace()
//  }

//
//  val nameOnWall = mark.map(profile => profile.name)
//
//  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
//
//  val zucksFilter = marksBestFriend.filter(profile => profile.name.startsWith("z"))

  // For comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks
  val profileNoMatterWat = SocialNetwork.fetchProfile("UnkownId").recover{
    case e:Throwable => Profile("fb.id.0.dummy","ForEver")
  }
  val pfetchedrofileNoMatterWat = SocialNetwork.fetchProfile("UnkownId").recoverWith{
    case e:Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
  }
  val fallbackResult = SocialNetwork.fetchProfile("unkonwn").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))


  // online banking app

  case class User(name:String)
  case class Transaction(sender:String,receiver:String,amount:Double,status:String)

  object BankingApp{
    val name = "Sreenu Banking"

    def fetchUser(name:String):Future[User] = Future{
      Thread.sleep(1000)
      User(name)
    }

    def createTransaction(user: User,marchantName:String,amount:Double):Future[Transaction] = Future{
      Thread.sleep(1000)
      Transaction(user.name,marchantName,amount,"SUccess")
    }

    def purchase(userName:String,item:String,marchantName:String,cost:Double):String = {
      // fetch the user
      // create a transaction and Wait for transaction done
      val transactionStatusFuture = for {
        user <- fetchUser(userName)
        transaction <- createTransaction(user,marchantName ,cost)
      } yield transaction.status

      Await.result(transactionStatusFuture,2.seconds) // implicit conversion
    }
  }
  println(BankingApp.purchase("Sreenu","Iphone","rock the sreenu",2000))

  // Promises

  val promise = Promise[Int]() // controller over a future
  val future1 = promise.future

  // Thread 1 - Consumer
  future1.onComplete{
    case Success(r) => println("[Consumer] I have received "+r)
  }

  // Thread 2 - producer
  val producer = new Thread(()=>{
    println("[Producer] crunching the numbers")
    Thread.sleep(500)
    promise.success(43)
    println("[Producer] produced")
  }).start()

  Thread.sleep(1000)

  /*
  *   1. fulfill a future immediately with a value
  *   2. inSequence(fa,fb)
  *   3. first(fa,fb)=> new future with the first value of the two futures
  *   4. last(fa,fb) => new future with last value
  *   5. retryUntil[T](action:() => Future[T], condition:T => Boolean):Future[T]
  * */

  // 1 - fulfill immediately

  def fulfillImmediately[T](value:T):Future[T] = Future(value)

  // 2. insequence
  def inSequence[A,B](first:Future[A],second:Future[B]):Future[B] =
    first.flatMap(_=>second)

  // 3 first out of two futures

  def first[A](fa:Future[A],fb:Future[A]):Future[A] = {
    val promise = Promise[A]
    def tryComplete(promise: Promise[A],result:Try[A]) = result match {
      case Success(a) => try{
        promise.success(a)
      }catch {
        case _ =>
      }
      case Failure(a) => try{
        promise.failure(a)
      }catch {
        case _ =>
      }
    }
    fa.onComplete(promise.tryComplete)
    fb.onComplete(tryComplete(promise,_))
    promise.future
  }

  // Retry intil

  def retryUntil[A](action:()=>Future[A],condition: A=>Boolean):Future[A] =
    action().
      filter(condition).recoverWith{
      case _ =>retryUntil(action,condition)
    }

}

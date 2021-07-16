package exercise

import scala.annotation.tailrec

object StreamsPlayGround extends App {

  val naturals = MyStream.from(1)(_+1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals
  println(startFrom0.head)

  startFrom0.take(10).foreach(print)

  println(startFrom0.map(_*2).take(10).toList())
  println(startFrom0.flatmap(x=> new Cons(x,new Cons(x+1,EmptyStream))).take(10).toList())

  println(startFrom0.filter(_ <10 ).take(10).toList())

  // Excersizes
    // 1 . Streams of infinite fibonacci number
    // 2 . Streams of prime numbers with Eartosthenes's sieve
      /*
      *  [ 2, 3 4 ...]
      *   filter out all numbers divisible by 2
      *   [2 3 5 7 8 11....]
      *   filter out all numbers divisible by 3
      *   [2 3 5 7 11 13 17 ..]
      *   filter out all numbers divisible by 5
      *
      * */

  /*
  *   [first , [ ....???
  *   [ firsr , fibo(second,fist+secind)
  * */
  def fibonacci(first:Int, second:Int) :MyStream[Int] = new Cons[Int](first,fibonacci(second,first+second))

  println("Fibonacci\n"+fibonacci(1,1).take(100).toList())

  /*
  *  [2,3,4,5,6,7,8,9,10...
  *   [ 2 eratosthenes applied to number filtered by n%2 !=0
  *   [ 2 3 eratosthenes applied to number filtered by n%3 !=0
  *   [ 2 3 5 eratosthenes applied to number filtered by n%5 !=0
  * */
  def eratosthenes(numbers:MyStream[Int]):MyStream[Int] = {
    if(numbers.isEmpty) numbers
    else new Cons[Int](numbers.head,eratosthenes(numbers.tail.filter(_ % numbers.head !=0)))

  }
  println("PrimeNumbers:"+eratosthenes(MyStream.from(2)(_ +1 )).take(20).toList())
}



abstract class MyStream[+A]{
  def isEmpty:Boolean
  def head:A
  def tail:MyStream[A]

  def #::[B >: A](element:B):MyStream[B] // Preprend
  def ++[B >: A](anotherStream: => MyStream[B]):MyStream[B]

  def foreach(f: A=>Unit): Unit
  def map[B](f: A=>B):MyStream[B]
  def flatmap[B](f: A=>MyStream[B]):MyStream[B]
  def filter(predicate: A=>Boolean):MyStream[A]

  def take(n:Int):MyStream[A]
  def takeAsList(n:Int):List[A] = take(n).toList()

  @tailrec
  final def toList[B >: A](acc:List[B] = Nil):List[B] =
    if(isEmpty) acc.reverse
    else tail.toList(head::acc)
}
object EmptyStream extends MyStream[Nothing]{
  def isEmpty:Boolean = true
  def head:Nothing = throw new NoSuchElementException
  def tail:MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element:B):MyStream[B]  = new Cons(element,this)
  def ++[B >: Nothing](anotherStream: => MyStream[B]):MyStream[B] =anotherStream

  def foreach(f: Nothing=>Unit): Unit = ()
  def map[B](f: Nothing=>B):MyStream[B] = this
  def flatmap[B](f: Nothing =>MyStream[B]):MyStream[B] = this
  def filter(predicate: Nothing =>Boolean):MyStream[Nothing] = this

  def take(n:Int):MyStream[Nothing] = this

}

class Cons[+A](hd:A,t1: => MyStream[A]) extends MyStream[A]{ // t1: is call by Name
  def isEmpty:Boolean = false
  override val head:A = hd
  override lazy val tail:MyStream[A] = t1 // call by need

  /*
  *  val s = new COns(1,EmptyStream)
  * val prepended = 1 #:: 4 new cons(1,s)
  * */
  def #::[B >: A](element:B):MyStream[B] = new Cons(element,this) // Preprend
  def ++[B >: A](anotherStream: => MyStream[B]):MyStream[B] = new Cons[B](head, tail ++ anotherStream) // call by Name

  def foreach(f: A=>Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def map[B](f: A=>B):MyStream[B] = new Cons(f(head),tail.map(f))
  def flatmap[B](f: A=>MyStream[B]):MyStream[B] = f(head) ++ tail.flatmap(f)
  def filter(predicate: A=>Boolean):MyStream[A] =
    if(predicate(head)) new Cons(head,tail.filter(predicate))
    else tail.filter(predicate)

  def take(n:Int):MyStream[A] = {
    if( n<=0 ) EmptyStream
    else if(n==1) new Cons(head,EmptyStream)
    else new Cons(head,tail.take(n-1))
  }



}

object MyStream{
  def from[A](start:A)(generator:A=>A): MyStream[A] = new Cons(start,MyStream.from(generator(start))(generator))
}

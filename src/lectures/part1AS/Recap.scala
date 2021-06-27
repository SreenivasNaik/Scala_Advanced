package lectures.part1AS

import scala.annotation.tailrec

object Recap extends App {
    val acondition:Boolean = false
    val aConditonval = if(acondition) 43 else 65

  // compiler infers types for us
    val acodeBlock = {
      if (acondition) 34
      54
    }

  // UNIT ==> void
  val theUnit = println("Hello")

  def aFunction(x:Int):Int = x+1

  // Recursion -> @tailrec
    @tailrec def factorial(n:Int,accumator:Int):Int =
      if(n<=0) accumator
      else factorial(n-1,n*accumator)

  // Object oriented programming
  class Animal
  class Dog extends Animal
  val adog:Animal = new Dog // SubTyping polymorphishm

  trait Carnivore{
    def eat(a:Animal):Unit
  }
  class Crocodile extends Animal with Carnivore{
    override def eat(a: Animal): Unit = println("Crunch")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(adog)
  aCroc eat adog  // natural language

  // Anonymous classes

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("Roar")
  }

  // generics
  abstract class MyList[+A] // Variance
  object MyList // singleTon and companions

  // case classes
  case class Person(name:String,age:Int)

  // Exceptions and try and catch finally
  val throwsException = throw new RuntimeException // This is type of Nothing

  val aFailure = try {
    throw  new RuntimeException
  }catch {
    case e: Exception => "I caught"
  }finally {
    println("Some logs")
  }

    // packaging and import
  // functional programming
  val incrementer = new Function[Int,Int] {
    override def apply(v1: Int): Int = v1+1
  }
  incrementer(1)

  val anymousIncementor = (x:Int)=>x+1
  List(1,2,3).map(anymousIncementor) // HOF MAP, flatmap filters

  // for - comprehensions
  val pairs = for {
    num <- List(1,2,4)
    char <- List('a','b','c')
  } yield num+"_"+char

  // scala Collections

  // Option ,Try

  // Pattern matching
  val x =2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case _=> "Other"
  }

  val bob = Person("Bob",22)

  val greeting = bob match {
    case Person(n,_) => s"MyName is $n"
  }
}

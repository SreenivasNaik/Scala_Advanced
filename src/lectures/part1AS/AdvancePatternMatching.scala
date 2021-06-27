package lectures.part1AS

object AdvancePatternMatching extends  App {

  val numbers = List(1)

  val description = numbers match {
    case head::Nil => println(s"The only Element is $head")
    case _ =>
  }

  /*
  *  Structures available for PM
  *   - constants
  *  - wildcards
  *  case classes
  *  tuples
  * */

  class Person(val name :String ,val age:Int)
  object Person{
    def unapply(person: Person):Option[(String,Int)] = {
      if (person.age<21) None
      else Some((person.name,person.age))
    }
    def unapply(age:Int):Option[String] =
      Some(if(age>21) "major" else "minor")
  }
  val bob = new Person(name ="Bob",age = 23)
  val greet = bob match {
    case Person(n,a) => s"Hi my name is $n  nad age $a"
  }
  println(greet)

  val legalAge = bob.age match {
    case Person(status) => s"Legal status is $status"
  }
  println(legalAge)

  object even{
    def unapply(arg:Int):Boolean =  arg %2 ==0
  }
  object singleDigit{
    def unapply(arg:Int):Boolean = arg < 10 && arg > -10

  }
  val n : Int = 452
  val matchProperty = n match {
    case singleDigit() => "single digit"
    case even() => "even number"
    case _=> "No property"
  }

  println(matchProperty)

  // Infix patterns

  case class Or[A,B](a:A,b:B)
  val either = Or(2,"two")
  val humanDescription  = either match {
   // case Or(number, string) => s"$number is Writtern as $string"
    case number Or string => s"$number is Writtern as $string"

  }
  println(humanDescription)

  // decomposing sequences

  val varArg = numbers match {
    case List(1,_*) => "starting with 1"
  }

  // Custom return types for unapply
  // isEmpty:Boolean get:Something

  abstract class Wrapper[T]{
    def isEmpty:Boolean
    def get:T
  }
  object PersonWrapper{
    def unapply(person: Person):Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false

      override def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(name)=> s"This name is $name"
    case _ => "nothing"
  })
}

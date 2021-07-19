package lectures.part4Implicits

object ImplicitsIntro extends App {



  val pair = "sreenu" -> "123"
  val intPair = 1 -> 2

  case class Person(name:String){
    def greet = s"Hi I am $name"
  }

  implicit def fromStringToPerson(str:String):Person = Person(str)

  println("Peter".greet) // println(fromStringToPerson("Peter").greet
//
//  class A{
//    def greet:Int = 2
//  }
//  implicit def fromStringToA(str:String):A

  // implicit parameters
  def increment(x:Int)(implicit  amount:Int) =x +amount
  implicit val defaultAmount = 10
  println(increment(10)) // Not as default args

}

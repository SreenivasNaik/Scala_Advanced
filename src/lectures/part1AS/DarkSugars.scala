package lectures.part1AS

import scala.util.Try

object DarkSugars extends App {

  // 1. Methods with single parameter

  def singleArgMethod(arg:Int):String = s"$arg litter"

  val description = singleArgMethod{
    // write someCode
    42
  }
  val aTryInstance = Try{
    throw new RuntimeException
  }

  List(1,2,3).map{
    x=> x+1
  }

  // 2. Single abstract method

  trait Action {
    def act(x:Int):Int
  }
  val aInstance :Action = new Action {
    override def act(x: Int): Int = x+1
  }

  val aFunckyInstance :Action = (x:Int) =>x+1
  // Example
  val aThread = new Thread(new Runnable {
    override def run(): Unit = print("Hello ")
  })

  val aSweetThread = new Thread(()=>print("Hello"))

  abstract class AnAbstractType{
    def implemented:Int = 23
    def f(a:Int):Unit
  }
  val abstractInstacne:AnAbstractType = (a:Int) => print("Sweet")

  // 3. the :: and #:: methods are special

  val prependedList = 2::List(3,5)
  // List(3,4).::(2)  => scala spec: last char decides associativity of method
  1:: 2 :: 3:: List(4,5)
  List(4,5).::(3).::(2)

  class MyStream[T]{
    def -->:(value:T):MyStream[T] = this
  }
  val myStream = 1 -->:2 -->:3 -->: new MyStream[Int]

  // 4. multi-word method nameing
  class TeanGirl(name:String){
    def `and then said`(gossip:String) : Unit = println(s"$name said $gossip")
  }

  val lilly = new TeanGirl("Lilly")
  lilly `and then said` "scala is so swwt"


  // 5. infix types

  class Composite[A,B]
  //val composite :Composite[Int,String] = ???
  val composite :Int Composite String = ???

  class -->[A,N]
  val towards:Int -->String = ???

  // 6 . update() is very special , much like apply()
  val anArray = Array(1,2,3)
  anArray(2) = 7 // ReWritten to anArray.update(2,7) ==> used in mutable collection


  //7. Setters for mutable containers
  class Mutable{
    private var internalMember:Int = 0
    def member:Int = internalMember // getters
    def member_=(value:Int):Unit = internalMember= value // setters
  }
  val aMutable = new Mutable
  aMutable.member = 42  // rewritten as Amutable.member_(42)

}

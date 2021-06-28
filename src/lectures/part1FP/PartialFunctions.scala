package lectures.part1FP

object PartialFunctions extends App {

  val aFunction = (x:Int) => x+1 // Function[Int,Int] === Int => Int
  val aFussyFunction = (x:Int) =>
    if(x == 1) 42
    else if (x ==2 ) 32
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNiceFussyFunction = (x:Int) => x match {
    case 1 => 42
    case 2 => 32
  }

  val aPartialFunction : PartialFunction[Int,Int] = {
    case 1 => 42
    case 2 => 32
  }
  println(aPartialFunction(2))
 // println(aPartialFunction(22))

  // PF utilities
  println(aPartialFunction.isDefinedAt(53)) // => for check whether given number is applicable or not

  // lift
  val lifted = aPartialFunction.lift // Int =>Option[Int]
  println(lifted(2))
  println(lifted(23))

  // chaining
  val pfChain = aPartialFunction.orElse[Int,Int] {
    case 45 => 45
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extends normal functions
  val totalFunction:Int=>Int = {
    case 1 =>99
  }
  //HOFs accepts partial functions also

  val MappedList = List(1,2,3).map{
    case 1 => 232
    case 2=>23
    case 3 =>43
  }
  println(MappedList)



  val manualFussyFunction = new PartialFunction[Int,Int] {
    override def isDefinedAt(x: Int): Boolean =
      x==1 || x==2 || x==3

    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 32
      case 3 =>23
    }
  }

  val chatBot:PartialFunction[String,String] = {
    case "hello" => "Hello My name is Cnu"
    case "goodbye" => "once you start there is no end"
    case "call mom" => "Not able find the contact"
  }


  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)
}

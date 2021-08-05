package lectures.part4Implicits

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(val value:Int) extends AnyVal {
    def isEven:Boolean = value%2 ==0
    def sqrt:Double = Math.sqrt(value)

  }

  new RichInt(40).isEven
  40.isEven

  /*
  *   Enrich the String class
  *   - asInt
  *   -encrypt
  *
  *   keep enriching the Int class
  *   - times(function)
  *   ->
  * */

  implicit class RichString(string:String){
    def asInt:Int = Integer.valueOf(string)
    def ecrypt(chiperDistanse:Int):String = string.map(c=>(c+chiperDistanse).asInstanceOf[Char])
  }

  println("2".asInt)
  println("Sreenu".ecrypt(2))

  // "2"/4
  implicit def StringToInt(string: String):Int = Integer.valueOf(string)
  println("4"/2)
  
}

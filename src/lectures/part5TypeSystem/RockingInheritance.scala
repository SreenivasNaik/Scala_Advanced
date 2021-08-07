package lectures.part5TypeSystem

object RockingInheritance extends App {

  // convenience
  trait Writer[T]{
    def write(value:T):Unit
  }

  trait Closable{
    def close(status:Int):Unit
  }

  trait GenericStream[T]{
    def foreach(f: T=>Unit):Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closable):Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem

  trait Animal { def name:String}
  trait Lion extends Animal{
    override def name: String = "Lion"
  }
  trait Tiger extends Animal{
    override def name: String = "Tiger"
  }
//  class Mutant extends Lion with Tiger{
//    override def name: String = "Alean"
//  }
 class Mutant extends Lion with Tiger
  val m = new Mutant
  println(m.name)

  /*
  *   Mutant extend Animal with { override def name: String = "Lion"}
  *   with Animal with {  override def name: String = "Tiger" }
  *
  *   LAST OVERRIDE GETS PICKED IN THE DIAMOND PROBLEM
  * */


  // 3. SUPER PROBLEM + Type linearization
  trait Cold{
    def print = println("cold")
  }
  trait Green extends Cold{
    override def print: Unit = {
      println("grean")
      super.print
    }
  }
  trait Blue extends Cold{
    override def print: Unit = {
      println("Blue")
      super.print
    }
  }

  class Red {
    def print = println("red")
  }

  class White extends Red with Green with Blue{
    override def print: Unit = {
      println("White")
      super.print
    }
  }
  val color = new White
  println(color.print)
/*
*   output : White
Blue
grean
cold
*  AnyRef with Red with Cold with Green with Blue with White
* */
}

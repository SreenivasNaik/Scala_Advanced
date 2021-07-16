package lectures.part2AFP

object LazyEvalutions extends App {

  // LAZY delayes the evaluation of values
  lazy val x:Int = throw new RuntimeException

  lazy val y: Int = {
    println("Hello")
    43
  }

  println(y)
  println(y)
  // Lazy values are evaluated only once,
  // when they used at first time from next time it won't evaluate

  // examples of implications

  // side effect
  def sideEffectsCondition:Boolean ={
    println("Boo")
    true
  }
  def simpleCondition:Boolean = false
  lazy val lazyCondition = sideEffectsCondition
  println(if(simpleCondition && lazyCondition) "Yes " else "No") // Output is NO
  // as simplecCOndition is false in && operation it won't call lazyCondition

  // In conjuction with call by name

  def byNameMethod(n: => Int):Int = n+n+n+n+1
  def retriveMagicValue:Int ={
    //side effect or long computation
    Thread.sleep(1000)
    println("Waiting")
    42
  }
  println(byNameMethod(retriveMagicValue))
  // Use Lazy val
  def byNameMethodLazy(n: => Int):Int ={
    lazy val t = n;
    t+t+t+t+1}
  println(byNameMethodLazy(retriveMagicValue))

  // CALL by Need

  // Filtering with lazy vals

  def lessThan30(i:Int):Boolean = {
    println(s"$i is less than 30?")
    i<30
  }
  def greaterThan20(i:Int):Boolean = {
    println(s"$i is less than 20?")
    i>20
  }

  val numbers = List(1,24,40,5,25)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // Lazy
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println("\n "+gt20Lazy)
  gt20Lazy.foreach(println)

  // For comprehensions use withFilter with guards

 println( for{
    a <- List(1,2,3) if a%2 ==0 // Use lazy val
  } yield a+1
 )
  List(1,2,4).withFilter(_%2==0).map(_+1)



}

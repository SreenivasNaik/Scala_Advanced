package lectures.part2AFP

object CurriesPartialAppliedFunctions extends App {
 // curried Function :: A function which return another function as result
  val superAdder:Int => Int=>Int =
    x=>y => x+y

  val add3 = superAdder(3) // Int => Int = y => 3+y
  println(add3(5))
  println(superAdder(3)(5))

  //
  def curriedAdder(x:Int)(y:Int):Int = x+y // curried method

  val add4:Int=>Int = curriedAdder(4)

  def inc(x:Int):Int = x+1
  List(1,2,3).map(x=>inc(x)) // ETA-Expansion

  // Partial function applications

  val add5= curriedAdder(5)_ // INT =>Int

  // Exercises
  val simpleAddFunction = (x:Int,y:Int) => x+y
  def simpleAddMethod(x:Int,y:Int):Int = x+y
  def curriedAddMethod(x:Int)(y:Int):Int = x+y

  // add7: Int => Int = y =>y+7

  val add7 = (x:Int) => simpleAddFunction(7,x)
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7,_:Int)
  val add7_3 = curriedAddMethod(7)_ // PAF
  val add7_4 = curriedAddMethod(7)(_) // PAF
  val add7_5 = simpleAddFunction(7,_:Int) // ==> y =>simpleAddMethod(7,y)


  // underscores are powerfull

  def concatenator(a:String,b:String,c:String) = a+b+c
  val insertName = concatenator("Hello I am ",_:String,"how are you?")
  println(insertName("SreenU"))

  val fillTheBlanks = concatenator("Hello",_:String,_:String)
  println(fillTheBlanks("Sreenu","you are awesome"))

    /*
    * 1.  Process a list of numbers and return their string representations with different formats
    *     use the %4.2 f, %8.6f and %14.12f with curries formator functions
    */
  def curriedFormator(s:String)(number:Double):String = s.format(number)
  val numbers = List(Math.PI,Math.E,1,9.9,1.3e-12)

  val simpleFormat = curriedFormator("%4.2f")_ // lift
  val seriousFormat = curriedFormator("%8.6f")_
  val perciseFormat = curriedFormator("%14.12f")_

  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(perciseFormat))



    /* 2. difference between
    *     -> functions vs methods
    *     -> parameters: by-name vs 0-lambda
    *
    */

  def byName(n:Int):Int = n+1
  def byFunction(f:()=>Int) = f()+1

  def method:Int = 42
  def perenMethod():Int = 42
  byName(23) // ok
  byName(method) // ok
  byName(perenMethod())
  byName(perenMethod) // OK but beware ==> byName(parenMethod())
  //byName(()=>42) Not ok
  byName((()=>42)())// Ok
  //byName(perenMethod_) // Not ok

  //byFunction(43) not Ok
  //byFunction(method) //Not ok does not do ETA - expansion
  byFunction(perenMethod) // complier does ETA
  byFunction(()=>42)
  byFunction(perenMethod _) // also works does not need to add _


  /*
  *   calling byName and byFunction
  *   int
  * method
  *   parenMethod
  *   lamdba
  *   PAF
  *
  */


}

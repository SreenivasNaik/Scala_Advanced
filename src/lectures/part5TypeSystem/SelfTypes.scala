package lectures.part5TypeSystem

object SelfTypes extends App {

  // Self types are a way requiring a type to be mixed in

  trait Instrumentallist{
    def play():Unit
  }

  trait Singer{
    this:Instrumentallist => // SELF TYPE WHO EVER IMPLEMENTS SINGER HAS TO IMPLEMENT THE INSTRUMENTALlIST

    def sing():Unit
  }

  class LeadSinger extends Singer with Instrumentallist{
    override def play(): Unit = ???

    override def sing(): Unit = ???
  }

//  class VocalList extends Singer{
//
//  }

  val jems = new Singer with Instrumentallist{
     override def play(): Unit = ???

    override def sing(): Unit = ???
  }

  class giiter extends Instrumentallist{
    override def play(): Unit = ???
  }
  val erric = new giiter with Singer{
    override def sing(): Unit = ???
  }

  // Vs Inheritance

  class A
  class  B extends A// ==> B is an A

  trait T
  trait S { self: T =>} // S requires a T


  // CAKE PATTERN  =>> "DEPENDENCY INJECTION"

  class Component{
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent (val component: Component)

  trait ScalaComponent{
    // APi
    def action(x:Int) :String
  }
  trait ScalaDependentCOmpenent{ self: ScalaComponent =>
    def dependentAction(x:Int):String = action(x)+" this Rocket"
  }

  // Layer 1 - Small COmponents
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentCOmpenent with Picture
  trait Analytics extends ScalaDependentCOmpenent with Stats


  // Cyclical dependencies
//  class X extends Y
//  class Y extends X
//  illegal cyclic reference involving class X
//  class Y extends X

  trait X{ self: Y =>}
  trait Y{ self: X =>}

}

package lectures.part5TypeSystem

object StructuralTypes extends App {

  // structural types
  type JavaClosable = java.io.Closeable

  class HipStartCloable{
    def close():Unit = println("yeah I am closing")
  }

  //def closeQuitely(closable: JavaClosable or HipStartCloable) // Not possiable what if we make it

  type UnifiedClosable = {
    def close():Unit
  } // STRUCTURAL TYPE

  def closeQuitely(unifiedClosable:UnifiedClosable):Unit = unifiedClosable.close()

  closeQuitely(new JavaClosable{
    override def close(): Unit = ???
  })
  closeQuitely(new HipStartCloable)

  // TYPE REFINEMENTS

  type advancedClosable = JavaClosable{
  def closeSilently():Unit
  }
  class AdvanceJavaCLosable extends JavaClosable{
    override def close(): Unit = print("Java closes")
    def closeSilently():Unit =  print("Java silently ")
  }

  def closeShh(advanceJavaCLosable: AdvanceJavaCLosable):Unit = advanceJavaCLosable.closeSilently()
  closeShh(new AdvanceJavaCLosable)

  // using structual types as standalone types

  def altClose(closable: {def close():Unit}):Unit = closable.close()

  // Type - checking ==> duck typing

  type SoundMaker = {
  def makeSound():Unit
  }

  class Dog{
    def makeSound():Unit = println("Dog Sound")
  }
  class Car{
    def makeSound():Unit = println("Car Sound")
  }
  val dog:SoundMaker = new Dog
  val car:SoundMaker = new Car
  // static duck typing

  // caveat: based on reflection

  trait CBL[+T]{
    def head:T
    def tail:CBL[T]
  }
  class Human{
    def head:Brain = new Brain
  }
  class Brain{
    override def toString: String = "BRAIN"
  }
  def f[T](somethingWithAHead:{ def head:T} ):Unit = println(somethingWithAHead.head)

  // f is compatible with a CBL with a HUMAN ==> Yes f is


}


package lectures.part5TypeSystem

object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection{
    type AnimalType // Abstract type member
    type BoundedAnimal <: Animal
    type superBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  //val dog :ac.AnimalType = ???
  //val cat:ac.BoundedAnimal = new Cat
  val pup:ac.superBoundedAnimal = new Dog
  val cat:ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat:CatAlias = new Cat

  trait MyList{
    type T
    def add(element:T):MyList
  }

  class NonEmptyList(value:Int) extends MyList{
    override type T = Int
    def add(element:Int):MyList = ???
  }

  // .type
  type catType = cat.type
  val newCat:catType = cat
  //new catType


  trait MList{
    type A
    def head:A
    def tail:MList
  }
//  class customList(hd:String,tl:customList) extends MList with ApplicableToNumber {
//    override type A = String
//
//    override def head: String = hd
//    override def tail: MList = tl
//  }

  class IntList(hd:String,tl:IntList) extends MList{
    override type A = String

    override def head: String = hd
    override def tail: MList = tl
  }

  trait ApplicableToNumber{
    type A <:Number
  }

  // Numbers
  //Type members and type members constraints


}

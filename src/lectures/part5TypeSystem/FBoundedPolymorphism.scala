package lectures.part5TypeSystem

object FBoundedPolymorphism extends App {

//  trait Animal{
//    def breed:List[Animal]
//  }
//
//  class Cat extends Animal{
//    override def breed: List[Animal] = ??? // LIST[CAT]
//  }
//  class Dog extends Animal{
//    override def breed: List[Animal] = ??? // LIST[DOG]
//  }

  // Solution 1 - Naive

//  trait Animal{
//    def breed:List[Animal]
//  }
//
//  class Cat extends Animal{
//    override def breed: List[Cat] = ??? // LIST[CAT]
//  }
//  class Dog extends Animal{
//    override def breed: List[Cat] = ??? // LIST[DOG]
//  }

  // Solution 2 : F-Bounded Polymorphism
//  trait Animal[A <: Animal[A]]{ // Recursive type --> F bounded Polymorphism
//    def breed:List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat ]{
//    override def breed: List[Animal[Cat]] = ??? // LIST[CAT]
//  }
//  class Dog extends Animal[Dog]{
//    override def breed: List[Animal[Dog]] = ??? // LIST[DOG]
//  }
//
//    trait Entity[ E <: Entity[E]] // ORM
//
//  class Person extends Comparable[Person]{
//    override def compareTo(o: Person): Int = ???
//  }
//
//  class Crocodile extends Animal[Dog]{ // THis is not correct
//    override def breed: List[Animal[Dog]] = ???
//  }
//
  // soultion 3  FBP+ self types
//
//  trait Animal[A <: Animal[A]]{ self: A => // Recursive type --> F bounded Polymorphism
//    def breed:List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat ]{
//    override def breed: List[Animal[Cat]] = ??? // LIST[CAT]
//  }
//  class Dog extends Animal[Dog]{
//    override def breed: List[Animal[Dog]] = ??? // LIST[DOG]
//  }
//
//  trait Entity[ E <: Entity[E]] // ORM
//
//  class Person extends Comparable[Person]{
//    override def compareTo(o: Person): Int = ???
//  }
//
//  class Crocodile extends Animal[Crocodile]{
//    override def breed: List[Animal[Crocodile]] = ???
//  }
//
//  trait Fish extends Animal[Fish]
//  class Shark extends Fish{
//    override def breed: List[Animal[Fish]] = List(new Cod) // THis is wrong
//  }
//   class Cod extends Fish{
//     override def breed: List[Animal[Fish]] = ???
//   }

  // Exercise

  // Solution 4:  use type classes
//  trait Animal
//  trait CanBreed[A]{
//    def breed(a:A):List[A]
//  }
//  class Dog extends Animal
//  object Dog {
//    implicit object DogCanBreed extends CanBreed[Dog]{
//      override def breed(a: Dog): List[Dog] = List()
//    }
//  }
//  implicit class CanBreedOps[A](animal: A){
//    def breed(implicit canBreed: CanBreed[A]):List[A] = canBreed.breed(animal)
//  }
//
//  val dog = new Dog
//  dog.breed
//
// class Cat extends Animal
//  object Cat {
//    implicit object CatCanBreed extends CanBreed[Dog]{
//      override def breed(a: Dog): List[Dog] = List()
//    }
//  }
//  val cat = new Cat
  //cat.breed

  // soultion 5 : PURE type classes

  trait Animal[A] {
    def breed(a:A):List[A]
  }
  class Dog
  object Dog{
    implicit object DogAnimal extends Animal[Dog]{
      override def breed(a: Dog): List[Dog] = List()
    }
  }
  implicit class AnimalOps[A](animal: A){
    def breed(implicit animalTypeclassInstance:Animal[A]):List[A] =
      animalTypeclassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

}

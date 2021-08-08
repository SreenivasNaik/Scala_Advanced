package lectures.part5TypeSystem

object Variance extends App {
  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  /*what is variance*
    "Inheritance" => type substitution of generics

   */

  class Cage[T]
  // Should Cage[Cat] also inherit Cage[Animal]
  // yes : Covariance
  class CCage[+T]
  val ccage:CCage[Animal] = new CCage[Cat]

  // No : Invariance
  class Icage[T]
  //val icage:Icage[Animal] = new Icage[Cat]
  // val x:Int = "hello"

  // Hell No - Opposite - Contra Variance
  class Xcage[-T]
  val xcage:Xcage[Cat] = new Xcage[Animal]


  class InvariantCage[T](val animal: T)// Invariant
  //Covariant positions

  class CovariantCage[+T](val animal: T) // Covariant Position

  //class ContraVeriantCage[-T](val animal: T)
    /*
    *  val catCage:XCage[Cat] = new Xcage[Animal](new Crocodile)
    * */
 // class CovariantVariableCage[+T](var animal: T) // Types of vars in CONTRAVARIANT POSITIONS
  /*
    VAL CCAGE:Cage[Anima;] = new Ccage[Cat](new Cat)
    cccage.animal = new Crocodile
  * */

  //class ContraVariantVariableCage[-T](var animal:T) // also in COVARIANT POSITION

  class InvariantVariableCage[T](var animal:T)
//
//  trait AnotherCOvariantCage[+T]{
//    def addAnimal(animal:T)// CONTRAVARIANT POSITION
//  }

  /*
  *  val cCage:CCage[Animal] = new CCage[Dog]
  *   ccage.add(new Cat)
  * */

  class AnotherCOntravariantCage[-T]{
    def addAnimal(animal:T) = true
  }
  val acc : AnotherCOntravariantCage[Cat] = new AnotherCOntravariantCage[Animal]
  acc.addAnimal(new Cat)

  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A]{
    def add[B >: A](element:B):MyList[B] = new MyList[B]// B super type of A [ B>: A] Type widening
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimal = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITIONS

    // return types

  class PetShop[-T]{
  //  def get(isItPuppy:Boolean):T // METHOD RETURN TYPES ARE COVARIANT POSITIONS
    /*
    *  VAL CatSHop = new PetShop[ANimal]{
    * def get(isItPuppy:Boolean):Animal = new Cat
    * }
    * val dogShop :PetShop[Dog] = catShop
    * dogshop.get(true) // Evil Cat
    * */

    def get [S <: T](isItPuppy:Boolean,defaultAnimal:S):S  = defaultAnimal
        // S is subClass oF T

  }
  val shop :PetShop[Dog] = new PetShop[Animal]
 // val evilCat = shop.get(true,new Cat)

  class TerraNova extends Dog
  val bigFurry = shop.get(true,new TerraNova)

  /* BIG RULES
    1. METHOD ARUGMENTS ARE IN CONTRAVARIANT POSITIONS
    2. RETURN TYPES ARE IN COVARIANT POSITIONS
  *
  * */

  /*
  *  Invariant , CoVarient, ContraVariant
  *  Parking[T](things:List[T]{
  *     park(vahicle:T)
  *     impound(vehicles:List[T]
  * checkVehicles(conditions:String):List[T]
  * }
  *
  *  2. Used someone else API : Ilist[T
  *   3. Parking = Monad
  *     flatmap
  * */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicle: List[T]){
    def park(vehicle: T):IParking[T] = ???
    def impound(vehicle: List[T]):IParking[T] = ???
    def checkVehicals(conditions:String):List[T] = ???
  }

  class CParking[+T](vehicle: List[T]){
    def park[S >: T](vehicle: S):CParking[S] = ???
    def impound[S >: T](vehicle: List[S]):CParking[S] = ???
    def checkVehicals(conditions:String):List[T] = ???
  }

  class ContraParking[-T](vehicle: List[T]){
    def park[T](vehicle: T):ContraParking[T] = ???
    def impound[T](vehicle: List[T]):ContraParking[T] = ???
    def checkVehicals[S <: T](conditions:String):List[S] = ???
  }

  /*
  Rule of THumb
   - Use COvariance = COLLECTION OF THINGS
   - USE CONTRAVARIANCE => GROUP OF ACTIONS
  *
  * */





}

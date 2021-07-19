package lectures.part4Implicits

object OrganizingImpicits extends App {

  implicit val reverseOrdering:Ordering[Int] = Ordering.fromLessThan(_>_)
 // implicit val normalOrdering:Ordering[Int] = Ordering.fromLessThan(_<_)
  println(List(1,4,5,2,3).sorted) // implicits in scala.Predef

  /*  Implicits:
        - val/var
         - objects
         - accessor methods - defs with no parentheses
  *
  * */

  // Excercises
    case class Person(name:String,age:Int)

  val persons = List(
    Person("sreenu",24),
    Person("naik",25),
    Person("sree",15)
  )
//  object Person{
//    implicit val alphabeticOrder:Ordering[Person] = Ordering.fromLessThan((s,b)=>s.name.compareTo(b.name) <0)
//  }
//  implicit val ageOrder:Ordering[Person] = Ordering.fromLessThan((s,b)=>s.age < b.age)
   object AplhabetOrder{
    implicit val alphabeticOrder:Ordering[Person] = Ordering.fromLessThan((s,b)=>s.name.compareTo(b.name) <0)
  }
  object AgeOrder{
    implicit val ageOrder:Ordering[Person] = Ordering.fromLessThan((s,b)=>s.age < b.age)
  }
  import AgeOrder._
 println( persons.sorted)

  /*
  *   1. Total Price = most used
  *   2. by unit count = 25%
  *   3. by unit price = 25%
  * */
  case class Purchase(nUnits:Int,unitPrice:Double)

  /* Implicits Scope
    -> Normal scope = LOCAL SCOPE
    -> Imported Scope
    -> companions of all the types involved in the method signature

  *
  * */
  object Purchase{
    implicit val totalPriceOrdering : Ordering[Purchase] = Ordering.fromLessThan((a,b)=> a.nUnits*a.unitPrice < b.nUnits*b.unitPrice)
  }

  object UnitCOuntOrdering{
    implicit val unitCountOrdering : Ordering[Purchase] = Ordering.fromLessThan((a,b)=> a.nUnits < b.nUnits)
  }
  object unitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }

}

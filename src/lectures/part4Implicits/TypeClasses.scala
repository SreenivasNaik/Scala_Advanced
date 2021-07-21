package lectures.part4Implicits

object TypeClasses extends App{


  trait HTMLWritable{
    def toHtml:String
  }
  case class User(name:String,age:Int,email:String) extends HTMLWritable{
    override def toHtml: String = s"<div>$name ($age yo) <a href = $email/> </div>"
  }

  User("Sreenu",25,"sree@nami.com").toHtml
  /* disadvantages
  *   1- works only for we write
  *   2. One Implementation out of a quite a number
  * */
  // Option 2 - Pattern matching
  object HTMLSerialization {
    def serializeToHTMl(value:Any) = value match {
      case User(n,a,b) =>
   //   case java.util.Date =>
    }
  }
  /* Disadvantage
    1. Lost Type safety
    2. Need to modify the code each time
    3. Still one implementation only
  * */

  trait HTMLSerializer[T] {
    def serialize(value:T) : String
  }
  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(value: User): String =s"<div>${value.name} (${value.age} yo) <a href = ${value.email}/> </div>"
  }

  println(UserSerializer.serialize(User("sreenu",24,"sdsd")))
  /* Advantages with this approach
  * 1. we can define seializers for the other types
  * */
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date]{
    override def serialize(value: Date): String = s"<div>${value.toString} </div>"
  }
  // we can define multiple serializer
 implicit object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(value: User): String =s"<div>${value.name}  </div>"
  }

  // TYPE CLASS -> Specifies the set of operations that can apply on the specific types
  // Singleton objects
  trait MyTypeClassTemplate[T]{
    def action(value:T):String
  }
  object MyTypeClassTemplate{
    def apply[T](implicit instance:MyTypeClassTemplate[T])= instance
  }

  // apply implicits

  object HTMLSerializer{
    def serialize[T](value:T)(implicit serializer: HTMLSerializer[T]):String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"div style clor value $value"
  }
  //println(HTMLSerializer.serialize(42)(IntSerializer))
  println(HTMLSerializer.serialize(43))
  val user = User("Sreenu",2,"Sdsd")
  println(HTMLSerializer.serialize(user))

  println(HTMLSerializer[User].serialize(user))

  /*
  1* Implement the TC pattern for the Equality type class*/
  /* Equlity
  * */

  trait Equal[T]{
    def apply(a:T,b:T):Boolean
  }
  object Equal{
    def apply[T](a:T,b:T)(implicit equalizer:Equal[T]):Boolean = equalizer.apply(a,b)
  }
  implicit object NameEquals extends Equal[User]{
    override def apply(a: User, b: User): Boolean = a.name == b.name}

  object FullEquals extends Equal[User]{
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }
  val anotherUser = User("sreenu",23,"Ssds")

  println(Equal(user,anotherUser))
  // Ad hoc polimorphism

}

package exercise

import lectures.part4Implicits.TypeClasses.{User, user}

object EqualityPlayGround extends App {
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
  val anotherUser = User("Sreenu",23,"Ssds")
  val user = User("Sreenu",2,"Sdsd")
  println(Equal(user,anotherUser))

  /*
  *  Excerise -> Improve the Equal TC with a n implicits conversioin classes
  * ===(anotherValue:T)
  * !==(anotherValue:T)
  * */

  implicit class TypeSafeEqual[T](value:T){
    def ===(anotherValue:T)(implicit equalizer:Equal[T]):Boolean = equalizer.apply(value,anotherValue)
    def !==(anotherValue:T)(implicit equalizer:Equal[T]):Boolean = ! equalizer.apply(value,anotherValue)

  }

  println(user === anotherUser)

  /* user.=== anotherUser
    newTypesafeEqual[User](user).===(anotherUser)
    newTypesafeEqual[User](user).===(anotherUser)(NameEquality)
  *
  * */
  // type safe

}

package exercise

import lectures.part4Implicits.TypeClasses.{User, user}

object EqualityPlayGround {
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



}

package lectures.part5TypeSystem

object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  trait MyList[T]{
    def flatmap[B](f: T=> B): MyList[B]
  }
  trait MyOption[T]{
    def flatmap[B](f: T=> B): MyOption[B]
  }
  trait MyFuture[T]{
    def flatmap[B](f: T=> B): MyFuture[B]
  }
//
//  def multiply[A,B](listA:List[A],listB:List[B]):List[(A,B)] = for {
//    a <- listA
//    b <- listB
//  } yield (a,b)
//
//  def multiply[A,B](listA:Option[A],listB:Option[B]):Option[(A,B)] = for {
//    a <- listA
//    b <- listB
//  } yield (a,b)

  // Use HKT

  trait Monad[F[_],A]{
    def flatMap[B](f: A=>F[B]):F[B]
    def map[B](f: A=>B):F[B]
  }
  implicit class MonadList[A](list:List[A]) extends Monad[List,A]{
    override def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)

    override def map[B](f: A => B): List[B] = list.map(f)
  }
  implicit class MonadOption[A](option: Option[A]) extends Monad[Option,A]{
    override def flatMap[B](f: A => Option[B]): Option[B] = option.flatMap(f)

    override def map[B](f: A => B): Option[B] = option.map(f)
  }
  def multiply[F[_],A,B](implicit ma:Monad[F,A],mb:Monad[F,B]):F[(A,B)] =
    for {
    a <- ma
    b <- mb
  } yield (a,b)
  //val monadList = new MonadList
//  monadList.flatMap(x=>List(x,x+1)) // List[Int]
println(multiply(new MonadList(List(1,2)),new MonadList(List("a","b"))))
println(multiply(List(1,2),List("c","d")))
println(multiply(Some(2),Some("a")))
}

package lectures.part2AFP

object Monads extends App {

  trait Attempt[+A]{
    def flatmap[B](f: A=> Attempt[B]):Attempt[B]
  }
  object Attempt{
    def apply[A](a: => A):Attempt[A] = try{
      Success(a)
    }catch {
      case e: Throwable => Fail(e)
    }
  }

  case class Success[+A](value:A) extends Attempt[A] {
    def flatmap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      }catch {
        case e:Throwable => Fail(e)
      }
  }
  case class Fail(value:Throwable) extends Attempt[Nothing]{
    def flatmap[B](f: Nothing=> Attempt[B]):Attempt[B] = this
  }

  val attemp = Attempt{
    throw  new RuntimeException("My Monad")
  }

  println(attemp)

  // 1. lazy Monads
  class Lazy[+A](value: => A){
    private lazy val internalValue = value
    def use:A = internalValue
    def flatmap[B](f:(=> A) => Lazy[B]):Lazy[B] = f(internalValue)
  }
  object Lazy{
    def apply[A](value: => A):Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy{
    println("HOOOO")
    43
  }

  println(lazyInstance.use)

  val flatmapInstance = lazyInstance.flatmap(x=> Lazy{
    10*x
  })
  val flatmapInstance2 = lazyInstance.flatmap(x=> Lazy{
    100*x
  })

  println(flatmapInstance.use)
  println(flatmapInstance2.use)


}

package lectures.part4Implicits

import java.{ util => ju}
object ScalaJavaCoversions extends App {

  import collection.JavaConverters._

  val javaSet :ju.Set[Int] = new ju.HashSet[Int]()
  (1 to 5).foreach(javaSet.add)
  println(javaSet)

  val scalaSet = javaSet.asScala
    /*
    * Iterator
    * Iterable
    * ju.List = scala.mutable.Buffer
    * ju.set = collection.mutable.set
    * ju.map = collection.mutable.Map
    *
    * */

  import collection.mutable._
  val numberBuffer = ArrayBuffer[Int](1,3,4)
  val juNumberBuffer = numberBuffer.asJava

  println(juNumberBuffer.asScala eq numberBuffer)

  val numberList = List(1,3,4)
  val juNumberList = numberList.asJava
  val backToScalaList = juNumberList.asScala
  println(backToScalaList == numberList) // true
  println(backToScalaList eq numberList) // false

  // create scala java optional - option

  class ToScala[T](value: => T){
    def asScala:T = value
  }
  implicit def asScalaOptional[T](o:ju.Optional[T]):ToScala[Option[T]] = new ToScala[Option[T]](
    if(o.isPresent) Some(o.get) else None
  )
  val juOptional:ju.Optional[Int] = ju.Optional.of(2)
  val scalaOption = juOptional.asScala

  println(scalaOption)

}

package exercise

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean ){
    /*
    *  implement functional set
    * */
  def contains(elem:A):Boolean
  def +(elem:A):MySet[A]
  def ++(anotherset:MySet[A]):MySet[A] // Union

  def map[B](f:A=>B): MySet[B]
  def flatmap[B](f:A=>MySet[B]): MySet[B]
  def filter(f:A=>Boolean): MySet[A]
  def foreach(f:A=>Unit):Unit

   def apply(v1: A): Boolean = contains(v1)
  def -(elem:A) :MySet[A]
  def --(anotherSet:MySet[A]):MySet[A] // difference
  def &(anotherSet:MySet[A]):MySet[A] // Intersection
  // Exercise #3 Implement unary_! = Negation of a set
  // set[1,2,3] =>
  def unary_! : MySet[A]

}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean =  false

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem,this)

  override def ++(anotherset: MySet[A]): MySet[A] = anotherset

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def flatmap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def filter(f: A => Boolean): MySet[A] = this

  override def foreach(f: A => Unit): Unit = ()

  /*
  *  Exercises
  * -> removing an element
  * intersection with another set
  * difference with another set
  * *

   */
  override def -(elem: A): MySet[A] = this

  override def &(anotherSet: MySet[A]): MySet[A] = this

  override def --(anotherSet: MySet[A]): MySet[A] = this

  override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}
//class AllInclusiveSet[A] extends MySet[A]{
//  override def contains(elem: A): Boolean = true
//
//  override def +(elem: A): MySet[A] = this
//
//  override def ++(anotherset: MySet[A]): MySet[A] = this
//
//  // all inclusive set [Int] => natuals.map(x=>x*3) ==> [0,1,2=
//  override def map[B](f: A => B): MySet[B] = ???
//
//  override def flatmap[B](f: A => MySet[B]): MySet[B] = ???
//
//  override def filter(f: A => Boolean): MySet[A] = ???//property based set
//
//  override def foreach(f: A => Unit): Unit = ???
//
//  override def -(elem: A): MySet[A] = ???
//
//  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
//
//  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
//
//  override def unary_! : MySet[A] = new EmptySet[A]
//}

// all the element of type A  which satisfy a property
class PropertyBasedSet[A](property:A=>Boolean) extends MySet[A]{
  override def contains(elem: A): Boolean = property(elem)

  override def +(elem: A): MySet[A] =
    // {x in A | property(x) } + element = {x in A | property(x) || x == element}
    new PropertyBasedSet[A](x=>property(x) || x == elem)

  override def ++(anotherset: MySet[A]): MySet[A] = new PropertyBasedSet[A](x=>property(x) || anotherset(x))

  override def map[B](f: A => B): MySet[B] = politelyFail

  override def flatmap[B](f: A => MySet[B]): MySet[B] = politelyFail

  override def filter(f: A => Boolean): MySet[A] = new PropertyBasedSet[A](x=>property(x) && f(x))

  override def foreach(f: A => Unit): Unit = politelyFail

  override def -(elem: A): MySet[A] = filter(x=> x != elem)

  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x=> !property(x))
  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole")
}
class NonEmptySet[A](head:A,tail:MySet[A]) extends MySet[A] {
  override def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)

  override def +(elem: A): MySet[A] =
    if(this contains elem) this
    else new NonEmptySet[A](elem,this)

  override def ++(anotherset: MySet[A]): MySet[A] =
    tail ++ anotherset + head


  override def map[B](f: A => B): MySet[B] =
    (tail map f)+ f(head)

  override def flatmap[B](f: A => MySet[B]): MySet[B] = (tail flatmap f) ++ f(head)

  override def filter(f: A => Boolean): MySet[A] = {
    val filterTail = tail filter f
    if(f(head)) filterTail + head
    else filterTail
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  override def -(elem: A): MySet[A] =
    if(head == elem) tail
    else tail - elem + head

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // Intersection == filtering
  override def --(anotherSet: MySet[A]): MySet[A] = filter(x=> !anotherSet(x))

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))

}

object MySet{
  def apply[A](values:A*):MySet[A] = {
    @tailrec
    def buildSet(valseq:Seq[A],acc:MySet[A]):MySet[A] = {
      if(valseq.isEmpty) acc
      else buildSet(valseq.tail,acc+valseq.head)

    }
    buildSet(values.toSeq,new EmptySet[A])
  }
}

object MysetPlay extends App{
  val s = MySet(1,2,3)
  s foreach println
  s+ 5 ++ MySet(-1,-2) + 3 map(x=>x*10) filter(x=>x%2==0) foreach print

  val negative = !s

  println(negative(2))
  println(negative(5))
}

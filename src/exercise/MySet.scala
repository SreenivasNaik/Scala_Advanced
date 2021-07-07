package exercise

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean ){
    /*
    *  implement functional set
    * */
  def contains(elem:A):Boolean
  def +(elem:A):MySet[A]
  def ++(anotherset:MySet[A]):MySet[A]

  def map[B](f:A=>B): MySet[B]
  def flatmap[B](f:A=>MySet[B]): MySet[B]
  def filter(f:A=>Boolean): MySet[A]
  def foreach(f:A=>Unit):Unit

   def apply(v1: A): Boolean = contains(v1)
}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean =  false

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem,this)

  override def ++(anotherset: MySet[A]): MySet[A] = anotherset

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def flatmap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def filter(f: A => Boolean): MySet[A] = this

  override def foreach(f: A => Unit): Unit = ()


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
}

package lectures.Part3Concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.immutable.ParVector

object ParallelUtils extends App {

  // 1. Parallel collections -> .par is keyword to use
  val parList = List(1,2,3).par

  val parVector = ParVector[Int](1,2,3)

  /*
  *   Seq
  *   Vector
  *   Array
  *   Map - Hash, Trie
  *   Set - Hash, Trie
  * */


  def messure[T](operation: => T):Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 1000).toList
  val serialTime = messure(list.map(_+1))
  println("Serial TIme: "+serialTime)
  val parallelTime = messure(list.par.map(_+1))


  println("parallelTime : "+parallelTime)

  // Parallel time will be good for huge collection as it will create the threads and run
  /*  Map - Reduce Model
      -> split the elements into chunks
      -> operations
      -> recombine - Combiner
  *
  * */

  // map,flatmap,filter are good but be carefull with reduce and fold as they are not associative operations
  println(List(1,2,3).reduce(_-_))
  println(List(1,2,3).par.reduce(_-_))
  // Synchronizaiton
  var sum =0
  List(1,2,3).foreach(sum+=_)
  println(sum)
  sum = 0
  List(1,2,3).par.foreach(sum+=_)
  println(sum)

  // configurations
  parVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))
  /*
  *   -> ThreadPoolTaskSUpport
  *   -> ExecutionCOntextTaskSupport
  * */


  // Atomic operations and references
  val atomic = new AtomicReference[Int](2)
  val currentValue= atomic.get() // Thread safe read
  atomic.set(4)  // thread safe write

  atomic.getAndSet(3)

  atomic.compareAndSet(2,4)

  atomic.updateAndGet(_+1) // Thread safe function run
  atomic.getAndUpdate(_+1)

  atomic.getAndAccumulate(12,_+_)
 atomic.accumulateAndGet(12,_+_)
}

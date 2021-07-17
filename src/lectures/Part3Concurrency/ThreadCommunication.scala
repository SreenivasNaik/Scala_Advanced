package lectures.Part3Concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  *  Producer and Consumer
  *   producer -> [ x ] -> Consumer
  * */

  class SimpleContainer{
    private var value :Int =0
    def isEmpty:Boolean = value==0
    def get:Int = {
      val result = value
      value = 0
      result
    }
    def set(newValue:Int) = value=newValue
  }

  def naiveProducerConsumer():Unit ={
    val container = new SimpleContainer

    val consumer = new Thread(()=>{
      println("Consumer Waiting")
      while (container.isEmpty){
        println("Consumer is Still Waiting")
      }
      println("Consumer I have Consumed "+container.get)
    })

    val producer = new Thread(()=>{
      print("Producer computing something")
      Thread.sleep(500)
      val value =43
      println("Producer I have produced the value:"+value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }
  //naiveProducerConsumer()

  //wait and notify

  def smartProdConsumer():Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(()=>{
      println("Consumer is waiting")
      container.synchronized{
        container.wait()
      }
      println("Consumer I have consumed"+container.get)
    })

    val producer = new Thread(()=>{
      println("Producer is hard work")
      Thread.sleep(2000)
      val  value = 32
      container.synchronized{
        println("Producer producing the value"+value)
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }
  //smartProdConsumer()

  /*
  *  producer -> [ ? ,? ? ] => consumer
  * */

  def prodConsProdcuerLargeBuffer():Unit = {
    val buffer : mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3
    val consumer = new Thread(()=>{
      val random = new Random()
      while (true){
        buffer.synchronized{
          if(buffer.isEmpty){
            println("Consumer buffer Empty: Waiting")
            buffer.wait()
          }
          // there must be atlease one Value in buffer
          val x = buffer.dequeue()
          println("Consumer I consumed:"+x)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(1000))
      }

    })

    val prodcuer = new Thread(()=>{
      val random = new Random()
      var i =0
      while(true){
        buffer.synchronized{
          if(buffer.size == capacity){
            println("Producer buffer is full:Waiting")
            buffer.wait()
          }
          // there must be at least one empty space in buffer
          println("Producer producing:"+i)
          buffer.enqueue(i)
          buffer.notify()
          i+=1

        }
        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    prodcuer.start()
  }
  //prodConsProdcuerLargeBuffer()

  /*
  *  Level 3: Multiple producers and Multiple consumers acting on same buffer
  * */

  class Consumer(id:Int,buffer:mutable.Queue[Int]) extends Thread{
    override def run(): Unit = {
      val random = new Random()
      while (true){
        buffer.synchronized{
          /*
          *  producer produces value, Two consumers are waiting
          * notifies One consumer notifes on buffer
          *  notifies other consumer
          * */
          while (buffer.isEmpty){
            println(s"[Consumer : $id] buffer Empty: Waiting")
            buffer.wait()
          }
          // there must be atlease one Value in buffer
          val x = buffer.dequeue()
          println(s"[Consumer : $id] I consumed:"+x)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }

    }
  }
  class Producer(id:Int,buffer:mutable.Queue[Int],capacity:Int) extends Thread{
    override def run(): Unit = {
      val random = new Random()
      var i =0
      while(true){
        buffer.synchronized{
          while (buffer.size == capacity){
            println(s"[Producer :$id] buffer is full:Waiting")
            buffer.wait()
          }
          // there must be at least one empty space in buffer
          println(s"[Producer :$id] producing:"+i)
          buffer.enqueue(i)
          buffer.notify()
          i+=1

        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCOns(nConsumer:Int,nProducer:Int):Unit ={
    val buffer : mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20
    (1 to nConsumer).foreach(i=> new Consumer(i,buffer).start())
    (1 to nProducer).foreach(i=> new Producer(i,buffer,capacity).start())
  }

  //multiProdCOns(3,3)

  /*
  *  Exercises
  *   1. Think of an example where notifyall acts in a different way
  *   2. Create a dead lock
  *   3. create a liveLock
  * */

   // NotifyAll
  def testnotifyAll():Unit = {
    val bell = new Object
    (1 to 10).foreach(i=> new Thread(()=>{
      bell.synchronized{
        println(s"[Thread: $i] waiting")
        bell.wait()
        println(s"[Thread: $i] woken")
      }
    }).start())

    new Thread(()=>{
      Thread.sleep(2000)
      println("[Annoncer] Rock")
      bell.synchronized{
        bell.notifyAll()
      }
    }).start()
  }
  //testnotifyAll()

  // DeadLock
  case class Friend(name:String){
    def bow(other:Friend) = {
      this.synchronized{
        println(s"$this :I am bowing to my friend $other")
        other.rise(this)
        println(s"$this :my friend $other has risen")
      }
    }
    def rise(friend:Friend): Unit ={
      this.synchronized{
        println(s"$this :I am rising $friend ")
      }
    }

    var side = "right"
    def switchSide():Unit ={
      if(side == "right") side = "left"
      else side = "right"

      }
    def pass(other:Friend):Unit = {
      while (this.side == other.side){
        println(s"$this: oh please $other, feel free to pass")
        switchSide()
        Thread.sleep(1000)
      }
    }
    }

  val sam = Friend("sam")
  val prince = Friend("prince")
  //new Thread(()=> sam.bow(prince)).start()
  //new Thread(()=> prince.bow(sam)).start()

  // 3 - Live Lock
  new Thread(()=>sam.pass(prince)).start()
  new Thread(()=>prince.pass(sam)).start()
}


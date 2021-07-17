package lectures.Part3Concurrency

import java.util.concurrent.Executors

object Intro extends App {
  // JVM Threads
  /*
  *   interface Runnable{
  *   public void run()
  * }
  * */
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in Parallel")
  })

  //aThread.start() // Gives the signal to the JVM to start a JVM thread
  // Don't call run method directly

  aThread.join() // block until Athread finishes running

  val threadHello = new Thread(()=> (1 to 5).foreach(_=>println("Hello")))
  val threadgdBye = new Thread(()=> (1 to 5).foreach(_=>println("Good Bye")))
  //threadHello.start()
  //threadgdBye.start()
  // Different Runs produce different results

  // Executors
  val pool = Executors.newFixedThreadPool(10)
  //pool.execute(()=>println("some thing the thread pool"))
  pool.execute(()=>{
    Thread.sleep(1000)
   // println("Done after 1 second")
  })
  pool.execute(()=>{
    Thread.sleep(1000)
 //   println("almost Done")
    Thread.sleep(1000)
  //  println("Done after 2 seconds")
  })

  pool.shutdown()
  //  pool.execute(()=>println("some thing the thread pool"))
  //  it will throw excpetion as we are submitting the task after shutdown

 // println("isShutDown"+pool.isShutdown)

  def runInParralel = {
    var x =0
    val thread1 = new Thread(()=>{
      x = 1
    })
    val thread2 = new Thread(()=>{
      x = 2
    })

    thread1.start()
    thread2.start()

    println("x"+x)

  }

  //for(_ <- 1 to 100) runInParralel
  // Race conditions

  class BankAccount(var ammount:Int){
    override def toString: String = "Amount:: "+ammount
  }
  def buy(account: BankAccount,thing:String,price:Int):Unit = {
    account.ammount -= price
  //  println("I have bought "+thing+"Current Account balance "+account)
  }

  for(_ <- 1 to 2){
    val account = new BankAccount(50000)
    val thread1 = new Thread(()=>buy(account,"shoes",3000))
    val thread2 = new Thread(()=>buy(account,"Iphone",4000))

    //thread1.start()
    //thread2.start()
    Thread.sleep(10)
   // if(account.ammount!= 43000) println("Aha::"+account.ammount)
   // println()

  }


   // to avoid  race conditions
  // use 1. synchronized
  def buySafe(account: BankAccount,thing:String,price:Int):Unit = {
      account.synchronized({
        account.ammount -= price
        println("I have bought "+thing+"Current Account balance "+account)
      }
      )
  }

  // Use Option2 = Volatile => use @volatile

  /*
  *   Exercises
  *   1. Construct 50 inception threads
  *   Thread1 => Thread2 => THread3
  *   print in reverse order
  * */

    def inceptionThread(maxThreads:Int,i:Int =1 ):Thread = new Thread(()=>{
      if(i< maxThreads) {
        val newThread = inceptionThread(maxThreads,i+1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from Thread $i")
    })
    inceptionThread(4).start()
}

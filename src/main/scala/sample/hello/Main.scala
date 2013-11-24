package sample.hello

import akka.actor.{ ActorSystem, Actor, Props, ActorLogging }
import scala.concurrent.duration._ // milliseconds
import scala.concurrent.ExecutionContext.Implicits.global // context.system.scheduler.schedule

import akka.util.Timeout
import scala.concurrent.Future;
import scala.concurrent.Await

case object Start

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem()
    
    /*
    val myActor = system.actorOf(Props[ScheduledActor], "Scheduler")
    val scheduler = system.scheduler.schedule(20 milliseconds, 0 milliseconds, myActor, "ping")
    Thread.sleep(1000)
    system.shutdown()*/

    implicit val timeout = Timeout(35000 millis)
    val futures = for (i <- 1 to 10) yield Future { Thread.sleep(i*100); i }
    val listOfFuture = Future.sequence(futures)

    system.actorOf(Props[HelloActor]) ! Start
    val results = Await.result(listOfFuture, timeout.duration)
    system.actorOf(Props[HelloActor]) ! Start

    system.shutdown()

    println(results)
    println("Hello!")
  }
}

class HelloActor extends Actor {
  val worldActor = context.actorOf(Props[WorldActor])
  def receive = {
    case Start ⇒ worldActor ! "Hello"
    case s: String ⇒
      println(s"Invoked after at ${System.currentTimeMillis()} ms");
      println("Received message: %s".format(s))
      //context.system.shutdown()
  }
}

class ScheduledActor extends Actor with ActorLogging {
  var nanos = System.nanoTime()

  def receive: Actor.Receive = {
    case msg => {
      nanos = System.nanoTime()
      println(s"Invoked after ${(System.nanoTime() - nanos)/1000000.0} ms");
    }
  }
}

class WorldActor extends Actor {
  def receive = {
    case s: String ⇒ sender ! s.toUpperCase + " world!"
  }
}



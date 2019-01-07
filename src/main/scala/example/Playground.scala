package example

import akka.{Done, NotUsed}
import akka.actor.ActorRef


object Playground {

  import akka.stream.scaladsl._

  import akka.actor.ActorSystem
  import akka.stream._

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext

  implicit val ec = ExecutionContext.global
  implicit val system = ActorSystem("TestSystem")
  implicit val materializer = ActorMaterializer()

  val s = Source.empty
  val s1: Source[Char, NotUsed] = Source("Hello world")
  val s2: Source[String, NotUsed] = Source.single("Hello mate")
  val s3 = Source.fromFuture(Future{"Hello from the future"})

  val s4 = Source.repeat(6)

  val source: Source[Int, ActorRef] =  Source.actorRef[Int](bufferSize = 0, OverflowStrategy.fail)

  val stringSourceWithInt: Source[Int, String] = {
    val init = ""
    val concat = (s: String) => s + "a"
    Source.repeat(NotUsed).map(_ ⇒ concat(init).length).mapMaterializedValue(_ => concat(init))
  }

  val useSource: Source[Int, Future[Unit]] = source.mapMaterializedValue(a => Future { a ! "Hello"})

  def run(actor: ActorRef) = {
    Future { Thread.sleep(300); actor ! 1 }
    Future { Thread.sleep(200); actor ! 2 }
    Future { Thread.sleep(100); actor ! 3 }
  }
  val s5: Source[Int, Future[Unit]] = Source
    .actorRef[Int](bufferSize = 0, OverflowStrategy.fail)
    .mapMaterializedValue(run)

  //val sink = Sink.actorRef[Int]

  val flow = Flow

  val printSink = Sink.foreach[Int](value => println(s"value: $value"))

  val s6: RunnableGraph[(Future[Unit], Future[Done])] = s5.toMat(printSink)(Keep.both)

  val s7 = s5 to printSink

  //val s7 = s5.mapMaterializedValue(???)

}

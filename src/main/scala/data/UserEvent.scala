/**
  */
package data
import java.time.LocalDateTime
import scala.util.Random
import scala.collection.immutable.Stream
import java.io.{File, PrintWriter}
import scala.util.{Try}

case class UserEvent(timestamp: LocalDateTime, uid: String)
object UserEvent {
  def parseDate(dateStr: String) =
    Try(LocalDateTime.parse(dateStr)).getOrElse(LocalDateTime.now())

  def apply(line: String): UserEvent = {
    val cols = line.split(",")
    UserEvent(parseDate(cols(0)),cols(1))
  }
}

object UserEvents {
  def randomUID = s"uid${Random.nextInt(25)}"
  def uuid = java.util.UUID.randomUUID.toString

  /**
    * returns an infinite stream of UserEvent items
    */
  def defaultStream: Stream[UserEvent] =
    Stream.iterate(UserEvent(LocalDateTime.now(),randomUID)) {e =>
        UserEvent(e.timestamp.plusSeconds(20), randomUID)
    }

  def generateFile(path: String, numEvents: Int): Unit = {
    val writer = new PrintWriter(new File(path))
    defaultStream
      .take(numEvents)
      .map( (e:UserEvent) => s"${e.timestamp}, ${e.uid}")
      .foreach(writer.println(_))
    writer.close()
  }

  def generateRandomFile(dir: String):Unit = {
    val path = s"${dir}/${uuid}"
    generateFile(path, 100)
  }
}

/*
REPL:
import data._
val events = UserEvents.defaultStream
events.take(5)

val dataDir = "/Users/esumitra/workspaces/scala/spark-streaming-kafka/data"
UserEvents.generateRandomFile(dataDir)

 */

/**
  * streaming url counts
  * using stateful calculations
  */

package example

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream._
import utils._
import org.apache.spark.streaming.Duration
import data._
/**
  * $SPARK_HOME/bin/spark-submit --class "example.TextFileStream" --master local[4] /Users/esumitra/workspaces/scala/spark-streaming-kafka/target/scala-2.11/spark-streams.jar /Users/esumitra/workspaces/scala/spark-streaming-kafka/checkpoint /Users/esumitra/workspaces/scala/spark-streaming-kafka/data
  * args0 - checkpoint dir
  * args1 - input dir
  */
object TextFileStream {
  def main(args: Array[String]): Unit = {
    val checkpointDir = args(0)
    val inDir = args(1)
    val ssc =
      SparkUtils
        .streamingContext(SparkStreamingConfiguration(
          "stream", None, Seconds(20)))
    ssc.checkpoint(checkpointDir)
    val fstream = fileStream(ssc, inDir)
    val batchTuples = uidCounts(fstream)
    val totalCounts = batchTuples.updateStateByKey(updateKeyCounts)
    val batchCounts = batchTuples.reduceByKey(_ + _)
    totalCounts.print()
    batchCounts.print()
    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }

  def fileStream(ssc: StreamingContext, dir: String): DStream[String] =
    ssc.textFileStream(dir)

  def updateKeyCounts(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] =
    runningCount match {
      case Some(x) => Some(x + newValues.sum)
      case None => Some(newValues.sum)
    }

  def uidCounts(fstream: DStream[String]): DStream[Tuple2[String, Int]] = {
    fstream
      .map(UserEvent(_))
      .map(e => (e.uid, 1))
      .cache
  }

}

/*
REPL:

 */

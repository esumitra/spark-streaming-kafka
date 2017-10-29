/**
  * kafka streaming example
  */

package example

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream._
import utils._
import org.apache.spark.streaming.Duration
import data._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/**
  * $SPARK_HOME/bin/spark-submit --class "example.KafkaStream" --master local[4] /Users/esumitra/workspaces/scala/spark-streaming-kafka/target/scala-2.11/spark-streams.jar 192.168.1.112:9092 topic1
  * args0 - kafka_host:kafka_port
  * args1 - topic
  */
object KafkaStream {

  def main(args: Array[String]): Unit = {
    val kafkaHostPort = args(0)
    val topic = args(1)
    val ssc =
      SparkUtils
        .streamingContext(SparkStreamingConfiguration(
          "stream", Some("local[*]"), Seconds(30)))

    val kstream = kafkaStream(ssc,kafkaHostPort, topic)
    val wc = userCounts(kstream)
    wc.print()
    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }

  def uuid = java.util.UUID.randomUUID.toString

  /**
    * returns a kafka direct stream
    */
  def kafkaStream(ssc: StreamingContext, conn: String, topicsStr: String): InputDStream[ConsumerRecord[String, String]] = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> conn,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> s"spark_consumer-${uuid}",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array(topicsStr)
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams))
    stream
  }

  def userCounts(kstream: InputDStream[ConsumerRecord[String, String]]): DStream[Tuple2[String, Int]] = {
    kstream
      .map(_.value)
      .map(UserEvent(_))
      .map(e => (e.uid, 1))
      .reduceByKey(_ + _)
  }

}

/*
REPL:
kafkadocker_kafka_1 kafkadocker_zookeeper_1
 */

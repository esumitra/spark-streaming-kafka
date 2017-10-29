/**
  * utilities for using using spark
  */

package utils

import org.apache.spark.sql.SparkSession
import org.apache.spark._
import org.apache.spark.streaming._

object SparkUtils {
  lazy val rspark =
    SparkSession
      .builder()
      .appName("Spark REPL")
      .master("local[*]")
      .getOrCreate()

  lazy val spark =
    SparkSession
      .builder()
      .appName("Lab Spark Session")
      .getOrCreate()

  /**
    * returns Spark Session for input configuration
    */
  def session(conf: SparkConfiguration): SparkSession = {
    val builder =
      SparkSession
        .builder()
        .appName(conf.name)

    // set master if supplied
    conf.masterConf.map(builder.master(_))

    // set config props if any
    conf.props.map { props =>
      for ((k,v) <- props) {
        builder.config(k, v)
      }
    }

    builder.getOrCreate
  }

  /**
    *  returns streaming context for input configuration
    */
  def streamingContext(strConf: SparkStreamingConfiguration): StreamingContext = {
    val conf =
      new SparkConf()
        .setAppName(strConf.name)

    // set master if supplied
    strConf.masterConf.map(conf.setMaster(_))

    new StreamingContext(conf, strConf.duration)
  }

  
}

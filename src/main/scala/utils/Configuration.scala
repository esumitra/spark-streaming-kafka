/**
  * application configuration
  */

package utils

import org.apache.spark.streaming.Duration

case class SparkConfiguration(
  name: String,
  masterConf: Option[String],
  props: Option[Map[String, String]])

case class SparkStreamingConfiguration(
  name: String,
  masterConf: Option[String],
  duration: Duration)

Sp


## Spark Streaming
This project provides samples for Spark Streaming with stateful calculations and integration with Kafka

 - **TextFileStream.scala**: basic streaming examples with stateful calculation
 - **KafkaStream.scala**: Kafka streaming integration

The project requires Java 8, Scala 2.11.8 and sbt 0.13.16 environment to run.

### Running the samples
 -  Compile and generate the  "fat" jar from the project
  `sbt assembly`

 - Run the spark job on locally or in the cloud
    e.g., `$SPARK_HOME/bin/spark-submit --class "example.KafkaStream" --master local[4] /fullpath/spark-streams.jar kafka_host:kafka_port topic`

### License
Copyright 2017, Edward Sumitra

Licensed under the Apache License, Version 2.0.


package com.sky.helloworld

import org.apache.spark.streaming.dstream.DStream
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import pureconfig._

object HelloWorldStreaming {

  val appName = "hello-streaming"
  @transient lazy val log = org.apache.log4j.LogManager.getLogger(appName)

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9092", // TODO from config
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer]
  )

  def main(args: Array[String]): Unit = {
    implicit val appConfig = loadConfig[Config] match {
      case Right(c) => c
      case Left(crfs) =>
        crfs.toList.foreach { crf => println(crf.description) }
        sys.exit(1)
    }

    withSparkStreaming { ssc =>
      val input = KafkaUtils.createDirectStream[String, String](
        ssc,
        PreferConsistent,
        Subscribe[String, String](List("input"), kafkaParams) // TODO input from config
      ).map(_.value())
      sayHelloStreaming(input) // TODO WRITE TO KAFKA!
    }
  }

  def withSparkStreaming(f: StreamingContext => Unit)(implicit appConfig: Config): Unit = {
    val sparkConf = new SparkConf()
      .setAppName(appName)
    val ssc = new StreamingContext(sparkConf, Duration(appConfig.spark.batchDuration.toMillis))

    f(ssc)

    ssc.start()
    ssc.awaitTermination()
  }

  def sayHelloStreaming(stream: DStream[String]): DStream[String] = {
    stream.map { line => {
      log.info(s"Processing '$line'")
      s"Hello, $line"
    }}
  }

}

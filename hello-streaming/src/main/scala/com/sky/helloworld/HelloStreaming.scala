package com.sky.helloworld

import org.apache.spark.sql.{Dataset, SparkSession}
import pureconfig._

object HelloStreaming {

  val appName = "hello-streaming"

  @transient lazy val log = org.apache.log4j.LogManager.getLogger(appName)

  def main(args: Array[String]): Unit = {
    implicit val appConfig = loadConfig[Config] match {
      case Right(c) => c
      case Left(crfs) =>
        crfs.toList.foreach { crf => println(crf.description) }
        sys.exit(1)
    }

    withSpark(namesFromKafka, greet, greetingsToKafka)
  }

  def withSpark[I,O](read: SparkSession => Dataset[I], process: Dataset[I] => Dataset[O], write: Dataset[O] => Unit)(implicit appConfig: Config): Unit = {
    val sparkSession = SparkSession.builder()
      .appName(appName)
      .getOrCreate()

    (read andThen process andThen write)(sparkSession)

    sparkSession.stop()
  }

  private def namesFromKafka(ss: SparkSession)(implicit appConfig: Config): Dataset[String] = {
    import ss.implicits._
    val df = ss.read.format("kafka")
      .option("kafka.bootstrap.servers", "kafka:9092") // TODO from config
      .option("subscribe", appConfig.inputTopic)
      .load()
    df.selectExpr("CAST(value AS STRING)").as[String]
  }

  private def greetingsToKafka(ds: Dataset[String])(implicit appConfig: Config): Unit = {
    ds.writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "kafka:9092") // TODO from config
      .option("topic", appConfig.outputTopic)
      .start().awaitTermination()
  }

  def greet(ds: Dataset[String]): Dataset[String] = {
    import ds.sparkSession.implicits._
    ds.map { line => s"Hello, $line" }
  }

}

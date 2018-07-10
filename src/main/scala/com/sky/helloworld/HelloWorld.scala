package com.sky.helloworld

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import pureconfig._

object HelloWorld {

  // should we set appname from config? then we need to extract the config
  @transient lazy val log = org.apache.log4j.LogManager.getLogger("spark-helloworld")

  def main(args: Array[String]): Unit = {
    implicit val appConfig = loadConfig[Config] match {
      case Right(c) => c
      case Left(crfs) =>
        crfs.toList.foreach { crf => println(crf.description) }
        sys.exit(1)
    }

    withSparkBatch { ss =>
      import ss.implicits._
      sayHelloDataset(ss.read.textFile(appConfig.inputDir))
        .write.save(appConfig.outputDir)
    }

//    withSparkStreaming { ssc =>
//      // TODO this should be for batch, not streaming!
//      val input = ssc.textFileStream(appConfig.inputDir)
//      sayHello(input).saveAsTextFiles(appConfig.outputDir)
//    }
  }

  def withSparkBatch(f: SparkSession => Unit)(implicit appConfig: Config): Unit = {
    val sparkSession = SparkSession.builder()
      .appName(appConfig.appName)
      .getOrCreate()

    f(sparkSession)

    sparkSession.stop()
  }

  def sayHelloDataset(ds: Dataset[String])(implicit encoder: Encoder[String]): Dataset[String] = {
    ds.map { line => s"Hello, $line" }
  }

//  def withSparkStreaming(f: StreamingContext => Unit)(implicit appConfig: Config): Unit = {
//    val sparkConf = new SparkConf()
//      .setAppName(appConfig.appName)
//      .setMaster(appConfig.spark.master)
//    val ssc = new StreamingContext(sparkConf, Duration(appConfig.spark.batchDuration.toMillis))
//
//    f(ssc)
//
//    ssc.start()
//    ssc.awaitTermination()
//  }

  def sayHelloStreaming(stream: DStream[String]): DStream[String] = {
    stream.map { line => {
      log.info(s"Processing '$line'")
      s"Hello, $line"
    }}
  }

}

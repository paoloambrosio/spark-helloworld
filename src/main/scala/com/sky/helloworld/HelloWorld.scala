package com.sky.helloworld

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Duration, StreamingContext}
import pureconfig._

object HelloWorld {

  def main(args: Array[String]): Unit = {
    implicit val appConfig = loadConfig[Config] match {
      case Right(c) => c
      case Left(crfs) =>
        crfs.toList.foreach { crf => println(crf.description) }
        sys.exit(1)
    }

    withSparkStreaming { ssc =>
      val input = ssc.textFileStream(appConfig.inputDir)
      sayHello(input).print()
    }
  }

  def sayHello(stream: DStream[String]): DStream[String] = {
    stream.map { line => s"Hello, $line" }
  }

  def withSparkStreaming(f: StreamingContext => Unit)(implicit appConfig: Config): Unit = {
    val sparkConf = new SparkConf()
      .setAppName(appConfig.appName)
      .setMaster(appConfig.spark.master)
    val ssc = new StreamingContext(sparkConf, Duration(appConfig.spark.batchDuration.toMillis))

    f(ssc)

    ssc.start()
    ssc.awaitTermination()
  }
}

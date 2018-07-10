package com.sky.helloworld

import org.apache.spark.sql.{Dataset, Encoder, SparkSession}
import pureconfig._

object HelloWorldDataset {

  val appName = "hello-dataset"

  @transient lazy val log = org.apache.log4j.LogManager.getLogger(appName)

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
  }

  def withSparkBatch(f: SparkSession => Unit)(implicit appConfig: Config): Unit = {
    val sparkSession = SparkSession.builder()
      .appName(appName)
      .getOrCreate()

    f(sparkSession)

    sparkSession.stop()
  }

  def sayHelloDataset(ds: Dataset[String])(implicit encoder: Encoder[String]): Dataset[String] = {
    ds.map { line => s"Hello, $line" }
  }

}

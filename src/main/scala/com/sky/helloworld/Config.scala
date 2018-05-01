package com.sky.helloworld

import scala.concurrent.duration.Duration

case class Config(appName: String, inputDir: String, outputDir: String, spark: SparkConfig)

case class SparkConfig(batchDuration: Duration, master: String)
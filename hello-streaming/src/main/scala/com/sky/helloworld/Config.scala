package com.sky.helloworld

import scala.concurrent.duration.Duration

case class Config(inputTopic: String, outputTopic: String, spark: SparkConfig)

case class SparkConfig(batchDuration: Duration)
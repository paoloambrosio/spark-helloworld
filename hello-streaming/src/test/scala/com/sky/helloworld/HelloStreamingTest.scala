package com.sky.helloworld

import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.scalatest.WordSpec


class HelloStreamingTest extends WordSpec with DatasetSuiteBase {

  import sqlContext.implicits._

  "sayHello" should {

    "greet every name" in {
      val input = List("A", "B").toDS
      val expected = List("Hello, A", "Hello, B").toDS
      assertDatasetEquals(expected, HelloStreaming.greet(input))
    }
  }
}

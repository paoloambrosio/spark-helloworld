package com.sky.helloworld

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.scalatest.WordSpec

class HelloWorldStreamingTest extends WordSpec with StreamingSuiteBase {

  "sayHelloStreaming" should {

    "greet every name in each input" in {
      val input = List(List("A", "B"), List("C"))
      val expected = List(List("Hello, A", "Hello, B"), List("Hello, C"))
      testOperation[String, String](input, HelloWorldStreaming.sayHelloStreaming _, expected)
    }
  }
}

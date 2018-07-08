package com.sky.helloworld

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.scalatest.WordSpec

class HelloWorldTest extends WordSpec with StreamingSuiteBase {

  "sayHello" should {

    "greet every line" in {
      val input = List(List("A", "B"), List("C"))
      val expected = List(List("Hello, A", "Hello, B"), List("Hello, C"))
      testOperation[String, String](input, HelloWorld.sayHelloStreaming _, expected)
    }
  }
}

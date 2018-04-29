package com.sky.helloworld

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.scalatest.WordSpec

class HelloWorldTest extends WordSpec with StreamingSuiteBase {

  "sayHello" should {

    import HelloWorld.sayHello

    "greet every line" in {
      val input = List(List("A", "B"), List("C"))
      val expected = List(List("Hello, A", "Hello, B"), List("Hello, C"))
      testOperation[String, String](input, sayHello _, expected)
    }
  }
}

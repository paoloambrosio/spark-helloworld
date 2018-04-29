import Versions._

name := "disco-spark-helloworld"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
    "org.apache.spark" %% "spark-streaming" % sparkVersion % Provided,
    "com.github.pureconfig" %% "pureconfig" % "0.9.1",
    "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.9.0" % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )

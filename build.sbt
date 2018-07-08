import Versions._

name := "disco-spark-helloworld"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
    "org.apache.spark" %% "spark-streaming" % sparkVersion % Provided,
    "org.apache.spark" %% "spark-sql" % sparkVersion % Provided,
    "com.github.pureconfig" %% "pureconfig" % "0.9.1",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )


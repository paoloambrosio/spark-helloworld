import Versions._

name := "disco-spark-helloworld"

scalaVersion := "2.11.12"

enablePlugins(JavaAppPackaging, UniversalDeployPlugin, DockerPlugin)

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "com.github.pureconfig" %% "pureconfig" % "0.9.1",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )

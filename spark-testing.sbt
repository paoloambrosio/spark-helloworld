import Versions._

parallelExecution in Test := false
fork in Test := true
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")

libraryDependencies ++= Seq(
  "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.9.0" % Test,
  "org.apache.spark" %% "spark-hive" % sparkVersion % Test // cannot be completely disabled in spark-testing-base
)
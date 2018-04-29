import Versions._

parallelExecution in Test := false
fork in Test := true
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")

libraryDependencies += "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.9.0" % Test

import Versions.sparkVersion

lazy val commonSettings = Seq(
    organization := "com.example",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.11.12",
    libraryDependencies ++= Seq(
        "com.github.pureconfig" %% "pureconfig" % "0.9.1",
        "org.scalatest" %% "scalatest" % "3.0.5" % Test
    )
)

lazy val sparkSettings = {
  val testSettings = Seq(
    parallelExecution in Test := false,
    fork in Test := true,
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled"),

    libraryDependencies ++= Seq(
      "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.9.0" % Test,
      "org.apache.spark" %% "spark-hive" % sparkVersion % Test // cannot be completely disabled in spark-testing-base
    )
  )

  Seq(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core"           % sparkVersion % Provided,
      "org.apache.spark" %% "spark-sql"            % sparkVersion,
      "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
    )
  ) ++ testSettings
}

lazy val root = (project in file(".")).aggregate(`hello-batch`, `hello-streaming`)

lazy val `hello-batch` = project.settings(commonSettings ++ sparkSettings)

lazy val `hello-streaming` = project.settings(commonSettings ++ sparkSettings)

import sbt.Keys.mappings
import com.typesafe.sbt.packager.docker.{Cmd, _}

enablePlugins(JavaAppPackaging, UniversalDeployPlugin, DockerPlugin)

// http://sbt-native-packager.readthedocs.io/en/latest/recipes/custom.html#sbt-assembly
mappings in Universal := {
  val universalMappings = (mappings in Universal).value
  val fatJar = (assembly in Compile).value
  val filtered = universalMappings filter {
    case (file, name) =>  ! name.endsWith(".jar")
  }
  filtered :+ (fatJar -> ("lib/" + fatJar.getName))
}
scriptClasspath := Seq( (jarName in assembly).value )

// https://github.com/big-data-europe/docker-spark/tree/master/submit
dockerBaseImage := "bde2020/spark-submit:2.2.0-hadoop2.7"

dockerCommands := dockerCommands.value.filter {
  case ExecCmd("ENTRYPOINT", args @ _*) => false
  case ExecCmd("CMD", args @ _*)        => false
  case x                            => println(x); true
} ++ {
  lazy val fatJar = (assembly / assemblyJarName).value
  (Compile / selectMainClass).value.toList.flatMap( mainClass => List(
    Cmd("ENV", "ENABLE_INIT_DAEMON", "false"),
    Cmd("ENV", "SPARK_APPLICATION_MAIN_CLASS", mainClass),
    Cmd("ENV", "SPARK_APPLICATION_JAR_LOCATION", "lib/" + fatJar),
    Cmd("CMD", "/bin/bash", "/submit.sh")
  ))
}

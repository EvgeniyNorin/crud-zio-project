import sbt._
object V {
  val zio = "2.0.12"
  val zio_http = "0.0.5"
  val pureconfig = "0.17.2"
  val zio_sql = "0.1.2"
  val circe_version = "0.14.1"
}

object Dependencies {
  val zio: List[ModuleID] = List(
    "dev.zio" %% "zio" % V.zio,
    "dev.zio" %% "zio-http" % V.zio_http,
    "dev.zio" %% "zio-sql-postgres" % V.zio_sql
  )

  val pureconfig: List[ModuleID] = List(
    //"com.github.pureconfig" %% "pureconfig-core" % V.pureconfig,
    "com.github.pureconfig" %% "pureconfig" % V.pureconfig
  )



  val circe: List[ModuleID] = List(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % V.circe_version)
}

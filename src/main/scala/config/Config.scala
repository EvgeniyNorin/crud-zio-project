package config

import pureconfig._
import pureconfig.generic.semiauto.deriveReader
import zio.http.ServerConfig
import zio.sql.ConnectionPoolConfig
import zio.{ULayer, ZIO, ZLayer, http}

import java.util.Properties

object Config {
  trait Service {
    def dbConfig: DbConfig
    def httpServiceConfig: HttpServerConfig
  }

  private val basePath = "app"
  private val source = ConfigSource.default.at(basePath)

  val live: ULayer[Config.Service] = {
    import ConfigImpl._
    ZLayer.fromZIO(
      ZIO.attempt(source.loadOrThrow[ConfigImpl]).orDie
    )
  }

  val serverLive: ZLayer[Any, Nothing, ServerConfig] = zio.http.ServerConfig.live(
    http.ServerConfig.default.port(source.loadOrThrow[ConfigImpl].httpServiceConfig.port)
  )

  val connectionPoolConfigLive: ZLayer[Config.Service, Throwable, ConnectionPoolConfig] =
    ZLayer(for {
      serverConfig <- ZIO.service[Config.Service]
    } yield (ConnectionPoolConfig(
      serverConfig.dbConfig.url,
      connProperties(serverConfig.dbConfig.user, serverConfig.dbConfig.password)
    )))

  private def connProperties(user: String, password: String): Properties = {
    val props = new Properties
    props.setProperty("user", user)
    props.setProperty("password", password)
    props
  }
}

case class ConfigImpl(
    dbConfig: DbConfig,
    httpServiceConfig: HttpServerConfig
) extends Config.Service

object ConfigImpl {
  implicit val configReader: ConfigReader[ConfigImpl] = deriveReader[ConfigImpl]
  implicit val configReaderHttpServerConfig: ConfigReader[HttpServerConfig] = deriveReader[HttpServerConfig]
  implicit val configReaderDbConfig: ConfigReader[DbConfig] = deriveReader[DbConfig]
}

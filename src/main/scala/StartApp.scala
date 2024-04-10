import api.HttpRoutes
import config.Config
import repo.CustomerRepositoryImpl
import zio.http.Server
import zio.sql.ConnectionPool
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

import scala.language.postfixOps

object StartApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val server = for {
      server <- zio.http.Server.serve(HttpRoutes.app)
    } yield server

    server.provide(
      Config.live,
      Server.live,
      Config.serverLive,
      CustomerRepositoryImpl.live,
      ConnectionPool.live,
      Config.connectionPoolConfigLive
    )
  }
}

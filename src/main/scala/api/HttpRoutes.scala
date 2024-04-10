package api

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn._
import io.circe.syntax.EncoderOps
import model.Customer
import repo.CustomerRepository
import zio.ZIO
import zio.http._
import zio.http.model.{Method, Status}

object HttpRoutes {
  val app: HttpApp[CustomerRepository, Response] =
    Http.collectZIO[Request] {
      /** Добавить проверку JWT токена
        */
      case Method.GET -> !! / "customers" =>
        CustomerRepository
          .findAll()
          .runCollect
          .map(chunk => chunk.toArray)
          .tapError(e => ZIO.logError(e.getMessage))
          .either
          .map {
            case Right(customers) => Response.json(customers.asJson.spaces2)
            case Left(e)          => Response.status(Status.InternalServerError)
          }

      /** Проверяем учетную запись на наличие в БД и выдаем JWT-токен
        */
      case req @ Method.POST -> !! / "auth" / "login" => ???

      /** Сохраняем в БД логин и зашифрованный пароль
        */
      case req @ Method.PUT -> !! / "auth" / "register" => ???
    }
}

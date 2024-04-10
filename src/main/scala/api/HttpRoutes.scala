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

      case req @ Method.POST -> !! / "add" / "customer" => ???

      case req @ Method.PUT -> !! / "update" / "customer" => ???

      case req @ Method.DELETE -> !! / "delete" / "customer" => ???
    }
}
